package com.project.chatapi.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(
  title = "Chat API", 
  description = """
  For testing in DEV:  
  - Admin username: `admin`  
  - Admin password: `admin123`\n\n
  Use /auth/login to get a JWT, then click "Authorize".
  """, 
  version = "v1")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApi30Config {
}