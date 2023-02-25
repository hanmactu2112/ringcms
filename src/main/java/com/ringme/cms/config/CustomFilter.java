package com.ringme.cms.config;

import com.ringme.cms.dto.UserSecurity;
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
        System.err.println("custom filter: da chay vao");
        System.err.println("custom filter: "+request.getRequestURI()+" "+request.getMethod());
        System.err.println(SecurityContextHolder.getContext().getAuthentication());
        if(SecurityContextHolder.getContext().getAuthentication() !=null){
            if (!request.getRequestURI().matches("/login")&&!request.getRequestURI().equals("/")
                    &&!request.getRequestURI().equals("/index")
                    &&!request.getRequestURI().equals("/logout")
                    &&!request.getRequestURI().equals("/captcha.jpg")
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSecurity){
                System.err.println("Custom filter have user: "+request.getRequestURI());
                UserSecurity userDetails = (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Set<String> router = userDetails.getRouter();
                String path = request.getRequestURI();
                boolean check = false;
                for (String str : router){
                    if (antPathMatcher.match(str,path)){
                        check =  true;
                    }
                }
                System.out.println("CustomFilter check:"+ check);
                if (check) {
//                    filterChain.doFilter(request,response);
                    request.getRequestDispatcher(path).forward(request,response);
                    return;
                }
                else {
//                    response.sendRedirect("/error");
//                    request.getRequestDispatcher(path).forward(request,response);
                    response.sendError(HttpStatus.FORBIDDEN.value(),"You not have access");
                }
            }
            else {
                request.getRequestDispatcher(request.getRequestURI()).forward(request,response);
//                filterChain.doFilter(request,response);
                return;
            }
        }
        else {
            request.getRequestDispatcher(request.getRequestURI()).forward(request,response);
//            filterChain.doFilter(request,response);
            return;
        }
    }
}
