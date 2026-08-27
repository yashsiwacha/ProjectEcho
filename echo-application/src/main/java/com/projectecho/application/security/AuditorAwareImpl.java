package com.projectecho.application.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        final Optional<String> auditor;
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            auditor = Optional.of("system");
        } else {
            auditor = Optional.of(authentication.getName());
        }

        return auditor;
    }
}
