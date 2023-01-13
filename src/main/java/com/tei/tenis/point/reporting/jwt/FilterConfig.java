package com.tei.tenis.point.reporting.jwt;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> filter = new FilterRegistrationBean<JwtFilter>();
        filter.setFilter(new JwtFilter());
        return filter;
    }
}
