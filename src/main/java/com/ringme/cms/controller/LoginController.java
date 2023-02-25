package com.ringme.cms.controller;

import com.ringme.cms.dto.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AntPathMatcher antPathMatcher;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSecurity);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSecurity){
            return "redirect:/index";
        }
        return "login";
    }
    @GetMapping("/login-error")
    public String loginerror(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error","Username or password not correct");
        System.err.println("vl chu");
        return "redirect:/login";
    }
    @GetMapping("/")
    public String getHome(){
        return "redirect:/index";
    }
    @PostMapping("/login")
    public String login(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error","Captcha invalid");
        System.err.println("vl chu");
        return "redirect:/login";
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
