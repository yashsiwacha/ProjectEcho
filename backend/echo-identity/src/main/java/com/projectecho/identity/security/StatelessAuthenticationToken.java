package com.projectecho.identity.security;

import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/** Custom authentication token for stateless JWT contexts. */
public class StatelessAuthenticationToken extends AbstractAuthenticationToken {

  private final String principalId;

  public StatelessAuthenticationToken(String principalId) {
    super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))); // Default role
    this.principalId = principalId;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return principalId;
  }
}
