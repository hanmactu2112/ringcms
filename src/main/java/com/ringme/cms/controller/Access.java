package com.ringme.cms.controller;

import com.ringme.cms.dto.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class Access {
    @Autowired
    private AntPathMatcher antPathMatcher;

    private boolean isAccess(HttpServletRequest request){
        UserSecurity userDetails = (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> router = userDetails.getRouter();
        System.out.println("dddddddddddddd");
        String path = request.getRequestURI();
        for (String str : router){
            if (antPathMatcher.match(str,path)){
                return true;
            }
        }
        return false;
    }

//    // Kiểm tra quyền của người dùng
//    private boolean hasRole(String role) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getAuthorities().stream()
//                .anyMatch(authority -> role.equals(authority.getAuthority()));
//    }
}
