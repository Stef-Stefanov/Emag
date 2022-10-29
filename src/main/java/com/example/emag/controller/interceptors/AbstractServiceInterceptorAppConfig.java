package com.example.emag.controller.interceptors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class AbstractServiceInterceptorAppConfig implements WebMvcConfigurer {
    @Autowired
    AbstractServiceInterceptor abstractServiceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(abstractServiceInterceptor);
    }
}
