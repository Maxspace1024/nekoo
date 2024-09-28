package com.brian.nekoo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允許所有路徑
            .allowedOrigins("http://localhost:3000", "http://localhost:5500", "https://nekoo.xyz") // 允許的來源
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
            .allowedHeaders("*") // 允許的標頭
            .allowCredentials(true); // 允許帶上憑證
    }
}

