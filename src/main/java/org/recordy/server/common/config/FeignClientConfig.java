package org.recordy.server.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "org.recordy.server.auth.service.impl")
@Configuration
public class FeignClientConfig {
}
