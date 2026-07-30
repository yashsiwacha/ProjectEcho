package com.projectecho.identity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projectecho.common.shared.ErrorCode;
import com.projectecho.common.shared.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    String message = "Authentication is required";
    if (request.getAttribute("jwtException") != null) {
      message = (String) request.getAttribute("jwtException");
    }

    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED, message);
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
