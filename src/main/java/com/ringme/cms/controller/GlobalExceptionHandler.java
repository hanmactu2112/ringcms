//package com.ringme.cms.Controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ModelAndView handleResponseStatusException(ResponseStatusException ex) {
//        if (ex instanceof ResponseStatusException && ((ResponseStatusException) ex).getStatus() == HttpStatus.valueOf(1000)){
//            ModelAndView modelAndView = new ModelAndView("login"); // tên trang lỗi cần trả về
//            modelAndView.addObject("error", "captcha invalid"); // thêm thông điệp lỗi vào model để hiển thị trên trang lỗi
//            System.out.println("allllo");
//            return modelAndView;
//        }
//
//        ModelAndView modelAndView = new ModelAndView("403");
//        return modelAndView;
//    }
//    @ExceptionHandler(CaptchaNotCorrectException.class)
//    public ModelAndView handleCaptchaNotCorrectException(HttpServletRequest request, CaptchaNotCorrectException ex) {
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("error", ex.getMessage());
//        System.out.println("CaptchaNotCorrectException.class");
//        mav.setViewName("login");
//        return mav;
//    }
//}
