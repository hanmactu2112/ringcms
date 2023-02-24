package com.ringme.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CaptchaFilter extends UsernamePasswordAuthenticationFilter {
    private static final String CAPTCHA_SESSION_KEY = "captcha";
    private static final String LOGIN_URI = "/login";
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login");

        System.err.println("request.getRequestURI(): "+request.getRequestURI());


        boolean isLOGIN_URI = LOGIN_URI.equals(request.getRequestURI());
        boolean isMethodPost = request.getMethod().equalsIgnoreCase("POST");
        boolean isSecurityContextHolderNull = SecurityContextHolder.getContext().getAuthentication() == null;
        // Kiểm tra nếu đây là yêu cầu đăng nhập
        if ( isLOGIN_URI && isMethodPost
                && isSecurityContextHolderNull) {
            // Lấy giá trị của captcha từ session
            String captcha = (String) session.getAttribute(CAPTCHA_SESSION_KEY);
            // Lấy giá trị của captcha được gửi lên từ form đăng nhập
            String captchaValue = request.getParameter("captcha");

            if (captcha == null || captchaValue == null || !captcha.equalsIgnoreCase(captchaValue)) {
                // Nếu captcha không đúng, xóa session và forward về trang đăng nhập với thông báo lỗi
                session.removeAttribute("captcha");
                request.getSession().setAttribute("error", "Captcha invalid");
                dispatcher.forward(request, response);
                SecurityContextHolder.clearContext();
                return;
            }

            // Nếu captcha đúng, tiếp tục xử lý đăng nhập
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
                Authentication authentication = authenticationManager.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException e) {
                // Forward về trang đăng nhập với thông báo lỗi
                request.getSession().setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
//                request.getRequestDispatcher("/login?error").forward(request, response);
                dispatcher.forward(request, response);
                return;
            }
        }

        // Tiếp tục filter chain
        chain.doFilter(request, response);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}