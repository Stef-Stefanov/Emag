package com.example.emag.controller.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AbstractServiceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle
            (HttpServletRequest request, HttpServletResponse response, Object handler) {
        // exactly two hours!.
        request.getSession().setMaxInactiveInterval(60*60*2);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
//todo if unused remove method
//        System.out.println("Post Handle method is Calling");
    }
    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object
                    handler, Exception exception) throws Exception {
//todo if unused remove method
//        System.out.println("Request and Response is completed");
    }
}
