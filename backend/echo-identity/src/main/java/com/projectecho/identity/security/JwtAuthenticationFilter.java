package com.projectecho.identity.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter that intercepts incoming HTTP requests, extracts the JWT, and establishes the security
 * context.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenValidator tokenValidator;

  public JwtAuthenticationFilter(JwtTokenValidator tokenValidator) {
    this.tokenValidator = tokenValidator;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = extractTokenFromRequest(request);

    if (StringUtils.hasText(token)) {
      try {
        String subject = tokenValidator.validateAndGetSubject(token);
        if (subject != null) {
          StatelessAuthenticationToken authentication = new StatelessAuthenticationToken(subject);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (JwtException ex) {
        // Preserve the validation failure reason in the request
        request.setAttribute("jwtException", ex.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
