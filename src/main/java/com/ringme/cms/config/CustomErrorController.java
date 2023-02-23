package com.ringme.cms.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Lấy mã lỗi HTTP từ request
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // Nếu mã lỗi là 404, chuyển hướng đến trang báo lỗi của riêng bạn
        if (status != null && Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            return "error404";
        }

        // Mặc định, chuyển hướng đến trang báo lỗi mặc định của Spring Boot
        return "403";
    }

}
