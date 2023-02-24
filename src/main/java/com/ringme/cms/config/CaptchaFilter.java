package com.ringme.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
        System.err.println("request.getRequestURI(): "+request.getRequestURI());

        // Kiểm tra nếu đây là yêu cầu đăng nhập
        if ("/login".equals(request.getRequestURI()) && request.getMethod().equalsIgnoreCase("POST")) {
            // Lấy giá trị của captcha từ session
            HttpSession session = request.getSession();
            String captcha = (String) session.getAttribute(CAPTCHA_SESSION_KEY);

            // Lấy giá trị của captcha được gửi lên từ form đăng nhập
            String captchaValue = request.getParameter("captcha");

            // Kiểm tra captcha
//            if (captcha == null || captchaValue == null || !captcha.equalsIgnoreCase(captchaValue)) {
//                // Nếu captcha không đúng, xóa session và forward về trang đăng nhập với thông báo lỗi
//                System.err.println("Captcha valid");
//                session.removeAttribute("captcha");
//                request.getSession().setAttribute("error","Captcha invalid");
//                response.sendRedirect("/login-error");
//            }
            if (captcha == null || captchaValue == null || !captcha.equalsIgnoreCase(captchaValue)) {
                // Nếu captcha không đúng, xóa session và forward về trang đăng nhập với thông báo lỗi
                session.removeAttribute("captcha");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
                request.getSession().setAttribute("error", "Captcha invalid");
                dispatcher.forward(request, response);
                return;
            }

//            // Nếu captcha đúng, tiếp tục xử lý đăng nhập
//            String username = request.getParameter("username");
//            String password = request.getParameter("password");
//
//            try {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
//                Authentication authentication = authenticationManager.authenticate(authToken);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } catch (AuthenticationException e) {
//                // Forward về trang đăng nhập với thông báo lỗi
//                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
//                request.getRequestDispatcher("/login?error").forward(request, response);
//                return;
//            }
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
