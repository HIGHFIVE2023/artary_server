package com.highfive.artary.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Custom403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());

        //JSON 요청 여부 확인
        String contextType = request.getHeader("Content-Type");

        boolean jsonRequest = contextType.startsWith("application/json");

        if(!jsonRequest){
            response.sendRedirect("/users/access-denied");
        }

    }
}
