package com.ringme.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

public class CaptchaFilter extends OncePerRequestFilter {
    private static final String CAPTCHA_SESSION_KEY = "captcha";
    private static final String LOGIN_URI = "/login";
    private static final String[] URLS_TO_IGNORE = {"/static/**"};
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".png")||path.startsWith("/images");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.err.println("CaptchaFilter: "+request.getRequestURI()+" "+request.getMethod());

        // Kiểm tra nếu đây là yêu cầu đăng nhập
        if (request.getMethod().equalsIgnoreCase("GET")){
            chain.doFilter(request, response);
        }
        else if ("/login".equals(request.getRequestURI()) && request.getMethod().equalsIgnoreCase("POST")) {
            // Lấy giá trị của captcha từ session
            HttpSession session = request.getSession();
            String captcha = (String) session.getAttribute(CAPTCHA_SESSION_KEY);
            System.err.println("CaptchaFilter: chay vao if Captcha filter");
            String captchaValue = request.getParameter("captcha");
            if (captcha == null || captchaValue == null || !captcha.equalsIgnoreCase(captchaValue)) {
                session.removeAttribute("captcha");
                System.err.println("CaptchaFilter + request: "+request.getParameter("username"));
                System.err.println("CaptchaFilter + request: "+request.getParameter("password"));
                request.getRequestDispatcher("/login").forward(request,response);
            }
            else {
                chain.doFilter(request, response);
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String path = request.getServletPath();
//        return path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".png")||path.startsWith("/images");
//    }


//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        System.err.println("CaptchaFilter: "+request.getRequestURI()+" "+request.getMethod());
//
//        // Kiểm tra nếu đây là yêu cầu đăng nhập
//        if (request.getMethod().equalsIgnoreCase("GET")){
//            chain.doFilter(request, response);
//        }
//        else if ("/login".equals(request.getRequestURI()) && request.getMethod().equalsIgnoreCase("POST")) {
//            // Lấy giá trị của captcha từ session
//            HttpSession session = request.getSession();
//            String captcha = (String) session.getAttribute(CAPTCHA_SESSION_KEY);
//            System.err.println("CaptchaFilter: chay vao if Captcha filter");
//            String captchaValue = request.getParameter("captcha");
//            if (captcha == null || captchaValue == null || !captcha.equalsIgnoreCase(captchaValue)) {
//                session.removeAttribute("captcha");
//                System.err.println("CaptchaFilter + request: "+request.getParameter("username"));
//                System.err.println("CaptchaFilter + request: "+request.getParameter("password"));
//                request.getRequestDispatcher("/login").forward(request,response);
//            }
//            else {
//                chain.doFilter(request, response);
//            }
//        }
//        else {
//            chain.doFilter(request, response);
//        }
//    }
//
//
//    @Override
//    @Autowired
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        super.setAuthenticationManager(authenticationManager);
//    }
}