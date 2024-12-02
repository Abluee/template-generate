package com.nbcb.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * 模板控制台应用
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Template Console API",
        version = "1.0",
        description = "Template generation and management system"
    )
)
@SpringBootApplication
public class TemplateConsoleApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemplateConsoleApplication.class, args);
    }
}
