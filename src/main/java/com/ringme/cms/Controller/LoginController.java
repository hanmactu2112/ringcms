package com.ringme.cms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;



@Controller
public class LoginController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AntPathMatcher antPathMatcher;

    @GetMapping({"/login","/"})
    public String login(){
        System.out.println(passwordEncoder.encode("123456"));
        return "login";
    }
    @GetMapping({"/403"})
    public String error(){
        System.out.println(passwordEncoder.encode("123456"));
        return "403";
    }
    @GetMapping("/index")
    @PreAuthorize("hasPermission('/index', 'READ')")
    public String index(HttpServletRequest httpServletRequest){
        System.out.println(httpServletRequest.getRequestURI());
//        if(true){
            return "index";
//        }
//        else return "redirect:/login";
    }


//    private boolean isAccess(HttpServletRequest request){
//        UserSercurity userDetails = (UserSercurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Set<String> router = userDetails.getRouter();
//        System.out.println("dddddddddddddd");
//        String path = request.getRequestURI();
//        for (String str : router){
//            if (antPathMatcher.match(str,path)){
//                return true;
//            }
//        }
//        return false;
//    }

    // Kiểm tra quyền của người dùng
    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> role.equals(authority.getAuthority()));
    }
}
