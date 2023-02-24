package com.ringme.cms.Exception;

public class CaptchaNotCorrectException extends RuntimeException {
    public CaptchaNotCorrectException() {
        super("Captcha is not correct");
    }
}

