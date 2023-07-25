package com.serinryu.springproject.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("로그인 실패");

        // You can handle different types of authentication failures and customize the response accordingly.
        if (exception instanceof BadCredentialsException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password.");
        } else if (exception instanceof LockedException) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your account has been locked.");
        } else if (exception instanceof DisabledException) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your account has been disabled.");
        } else if (exception instanceof AccountExpiredException) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your account has expired.");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed: " + exception.getMessage());
        }
    }
}
