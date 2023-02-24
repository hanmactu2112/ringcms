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
        System.out.println(request.getRequestURI()+" 1");
        System.out.println(SecurityContextHolder.getContext().getAuthentication() + " 2");
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            boolean isMatchesLogin = request.getRequestURI().matches("/login");
            boolean isMatchesURI = request.getRequestURI().equals("/");
            boolean isMatchesLogout = request.getRequestURI().equals("/logout");
            boolean isMatchesError = request.getRequestURI().equals("/error");
            boolean isMatchesCaptcha = request.getRequestURI().equals("/captcha.jpg");
            boolean isUserSecurity = SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSercurity;
// xoa authen

            if (!isMatchesLogin && !isMatchesURI
                    && !isMatchesLogout && !isMatchesError
                    && !isMatchesCaptcha
                    && isUserSecurity) {
                System.out.println(request.getRequestURI());
                UserSercurity userDetails = (UserSercurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Set<String> router = userDetails.getRouter();
                String path = request.getRequestURI();
                boolean check = false;
                for (String str : router) {
                    if (antPathMatcher.match(str, path)) {
                        check = true;
                    }
                }
                if (check) filterChain.doFilter(request, response);
                else {
//                    response.sendRedirect("/error");
                    response.sendError(HttpStatus.FORBIDDEN.value(), "You not have access");
                }
            } else filterChain.doFilter(request, response);
        } else filterChain.doFilter(request, response);
    }
}