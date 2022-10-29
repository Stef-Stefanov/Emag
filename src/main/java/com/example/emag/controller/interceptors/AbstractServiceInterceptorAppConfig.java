package com.example.emag.controller.interceptors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class AbstractServiceInterceptorAppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    AbstractServiceInterceptor abstractServiceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(abstractServiceInterceptor);
    }
}
