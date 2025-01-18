package com.uexcel.executive.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static com.uexcel.executive.service.ICheckinService.getTime;

public class CustomAccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
           response.setHeader("executive-room-access-denied", "Authentication failed");
           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
           String msg = (accessDeniedException.getMessage() != null && accessDeniedException.getMessage()!=null ) ?
                   accessDeniedException.getMessage() : "Authentication failed";
           String jsonResponse = String.format("{\"timestamp\": \"%s\",\"status\": \"%s\",\"error\": \"%s\",\"message\": \"%s\",\"apiPath\": \"%s\"}",
                   getTime(), HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN.getReasonPhrase(),msg, request.getRequestURI());
        response.setContentType("application/json,charset=utf-8");
        response.getWriter().write(jsonResponse);
    }


}
