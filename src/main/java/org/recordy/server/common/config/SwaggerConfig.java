package org.recordy.server.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("레코디 레코디 잉잉")
                .description("레코디 API 스웨거 입니다.")
                .version("v1");

        return new OpenAPI()
                .info(info);
    }
}
