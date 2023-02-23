package com.ringme.cms.config;

import com.ringme.cms.dto.UserSercurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CustomFilter extends OncePerRequestFilter {
    @Autowired
    private AntPathMatcher antPathMatcher;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("da chay vao");
        System.out.println(request.getRequestURI());
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        if(SecurityContextHolder.getContext().getAuthentication() !=null){
            if (!request.getRequestURI().matches("/login")&&!request.getRequestURI().equals("/")
                    &&!request.getRequestURI().equals("/logout")&&!request.getRequestURI().equals("/error")
                    &&!request.getRequestURI().equals("/captcha.jpg")
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSercurity){
                System.out.println(request.getRequestURI());
                UserSercurity userDetails = (UserSercurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Set<String> router = userDetails.getRouter();
                String path = request.getRequestURI();
                boolean check = false;
                for (String str : router){
                    if (antPathMatcher.match(str,path)){
                        check =  true;
                    }
                }
                if (check) filterChain.doFilter(request,response);
                else {
//                    response.sendRedirect("/error");
                    response.sendError(HttpStatus.FORBIDDEN.value(),"You not have access");
                }
            }
            else filterChain.doFilter(request,response);
        }
        else filterChain.doFilter(request,response);
    }
}
