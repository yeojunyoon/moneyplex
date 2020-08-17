package com.kakaopay.moneyplex.filter;

import com.kakaopay.moneyplex.constants.HeaderField;
import com.kakaopay.moneyplex.constants.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
@Component
public class CommonFilter implements Filter {
    private static final int URI_MAX_LENGTH = 5;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StatusCode statusCode = null;

        if(request.getHeader(HeaderField.ROOM_ID) == null || request.getHeader(HeaderField.USER_ID) == null){
            statusCode = StatusCode.E1300;
        }

        if(statusCode == null){
            try{
                Long.parseLong(request.getHeader(HeaderField.USER_ID));
            }catch (NumberFormatException nfe){
                statusCode = StatusCode.E1300;
            }
        }

        if(statusCode != null){
            errorResponse((HttpServletResponse) servletResponse, statusCode);
            return;
        }

        String[] uri = request.getRequestURI().split("/");
        if(URI_MAX_LENGTH == uri.length && uri[URI_MAX_LENGTH-1].length() != 3) {
            errorResponse((HttpServletResponse) servletResponse, StatusCode.E1300);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void errorResponse(HttpServletResponse response, StatusCode statusCode) throws IOException {
        String responseFormat = "{\"result\":false, \"description\":\"%s\"}";
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(String.format(responseFormat, statusCode.getDescription()));
        response.getWriter().flush();
    }
}