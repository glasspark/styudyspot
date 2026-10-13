package com.glasspark.studyspot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    //  http://localhost:8080/swagger-ui/index.html
    @Bean
    public OpenAPI studySpotOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StudySpot API")
                        .description("StudySpot 스터디룸 예약 서비스 API")
                        .version("v1"))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local Server"));
    }
}
