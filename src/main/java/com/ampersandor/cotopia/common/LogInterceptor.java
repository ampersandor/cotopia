package com.ampersandor.cotopia.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class LogInterceptor implements HandlerInterceptor {
    
    private final MyLogger myLogger;

    public LogInterceptor(MyLogger myLogger) {
        this.myLogger = myLogger;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        myLogger.setRequestURL(request);
        return true;
    }
}
