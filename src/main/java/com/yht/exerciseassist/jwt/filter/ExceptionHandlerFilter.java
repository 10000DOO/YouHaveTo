package com.yht.exerciseassist.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.exceoption.dto.ExceptionResponse;
import com.yht.exerciseassist.exceoption.error.UnacceptableJWTToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        }catch (UnacceptableJWTToken e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), "JWT token validation failed");

            mapper.writeValue(response.getWriter(), exceptionResponse);
        }
    }
}
