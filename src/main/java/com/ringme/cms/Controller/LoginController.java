package com.ringme.cms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
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
    public String index(HttpServletRequest httpServletRequest){
        System.out.println(httpServletRequest.getRequestURI());
        return "index";
    }
}
