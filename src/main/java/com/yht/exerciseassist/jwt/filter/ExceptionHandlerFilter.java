package com.yht.exerciseassist.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.exception.dto.ErrorMessageDto;
import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import com.yht.exerciseassist.exception.error.jwt.EmptyJWTTokenExcep;
import com.yht.exerciseassist.exception.error.jwt.ExpiredJWTTokenExcep;
import com.yht.exerciseassist.exception.error.jwt.InvalidJWTTokenExcep;
import com.yht.exerciseassist.exception.error.jwt.UnsupportedJWTTokenExcep;
import com.yht.exerciseassist.jwt.service.JWTService;
import com.yht.exerciseassist.util.ResponseResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (InvalidJWTTokenExcep | UnsupportedJWTTokenExcep
                 | EmptyJWTTokenExcep e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
            List<ErrorMessageDto> errorMessageDtos = new ArrayList<>();
            errorMessageDtos.add(errorMessageDto);
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), errorMessageDtos);

            mapper.writeValue(response.getWriter(), exceptionResponse);
        } catch (ExpiredJWTTokenExcep e) {
            String refreshToken = request.getHeader("RefreshToken");

            if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer")) {
                String token = refreshToken.substring(7);
                ResponseResult result = jwtService.refreshToken(token.substring(0, token.length() - 1));
                if (result.getStatus() == HttpStatus.OK.value()) {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    mapper.writeValue(response.getWriter(), result);
                } else {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    mapper.writeValue(response.getWriter(), result);
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
                List<ErrorMessageDto> errorMessageDtos = new ArrayList<>();
                errorMessageDtos.add(errorMessageDto);
                ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), errorMessageDtos);

                mapper.writeValue(response.getWriter(), exceptionResponse);
            }
        }
    }
}
