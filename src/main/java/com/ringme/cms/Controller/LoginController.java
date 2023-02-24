package com.ringme.cms.Controller;

import com.ringme.cms.dto.UserSercurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AntPathMatcher antPathMatcher;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping({"/login","/"})
    public String login(Model model, HttpServletRequest request){
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(request.getSession().getAttribute("error"));
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSercurity);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSercurity){
            return "redirect:/index";
        }
        model.addAttribute("error",request.getSession().getAttribute("error"));
        return "login";
    }

    @GetMapping({"/403"})
    public String error(){
        System.out.println(passwordEncoder.encode("123456"));
        return "403";
    }
    @GetMapping("/index")
    public String index(HttpServletRequest httpServletRequest){
        System.out.println(httpServletRequest.getRequestURI());

//        if(isAccess(httpServletRequest)){
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
    @GetMapping("/error404")
    public String showError(){
        return "403";
    }
    @GetMapping("/error405")
    public String showError405(){
        return "login";
    }
}
