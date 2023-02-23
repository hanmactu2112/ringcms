package com.ringme.cms.Controller;

import com.ringme.cms.dto.UserSercurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AntPathMatcher antPathMatcher;

    @GetMapping({"/login","/"})
    public String login(){
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSercurity);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSercurity){
            return "redirect:/index";
        }
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        @RequestParam String captcha, HttpSession session, Model model,HttpServletRequest httpServletRequest) {
        // Kiểm tra Captcha
        String captchaText = (String) session.getAttribute("captcha");
        if (!captcha.equalsIgnoreCase(captchaText)) {
            session.invalidate();
            model.addAttribute("error", "Invalid captcha");
            return "login";
        }
        else return "redirect:/index";
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
    @GetMapping("/error404")
    public String showError(){
        return "403";
    }
}
