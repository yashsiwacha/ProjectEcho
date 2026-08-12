package com.projectecho.application.config;

import com.projectecho.identity.infrastructure.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring Security filter that extracts and validates the JWT from the {@code Authorization} header.
 *
 * <p>On every incoming request this filter:
 *
 * <ol>
 *   <li>Reads the {@code Authorization: Bearer &lt;token&gt;} header.
 *   <li>Validates the token via {@link JwtService}.
 *   <li>Populates the {@link SecurityContextHolder} so downstream Spring Security rules can enforce
 *       role-based access.
 * </ol>
 *
 * <p>Conforms to ADR-006 (stateless JWT) and ADR-011 (no secrets in source control).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(final JwtService jwtService) {
        super();
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            token = authHeader.substring(BEARER_PREFIX.length());
        } else if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.isTokenValid(token)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("JWT validation failed for request: {}", request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }

        final String subject = jwtService.extractSubject(token);
        final String role = jwtService.extractRole(token);
        final String mappedAuthority = role != null ? role : "ROLE_USER";

        // Only set the authentication if not already set (respect existing auth context)
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            final UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            subject, null, List.of(new SimpleGrantedAuthority(mappedAuthority)));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Authenticated user: {} with role: {}", subject, mappedAuthority);
            }
        }

        filterChain.doFilter(request, response);
    }
}
