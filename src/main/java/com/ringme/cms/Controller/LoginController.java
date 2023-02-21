package com.ringme.cms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login(){
        System.out.println(passwordEncoder.encode("123456"));
        return "login";
    }
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
