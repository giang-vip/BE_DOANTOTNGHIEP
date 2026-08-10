package com.hungnhan.school_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String cleanContextPath = (contextPath == null || contextPath.isBlank()) ? "" 
                : (contextPath.endsWith("/") ? contextPath.substring(0, contextPath.length() - 1) : contextPath);
        
        String swaggerUrl = "http://localhost:" + port + cleanContextPath + "/swagger-ui/index.html";

        System.out.println("\n==================================================================");
        System.out.println("🚀 ỨNG DỤNG ĐÃ KHỞI ĐỘNG THÀNH CÔNG!");
        System.out.println("👉 Truy cập Swagger UI tại đây: " + swaggerUrl);
        System.out.println("==================================================================\n");
    }
}
