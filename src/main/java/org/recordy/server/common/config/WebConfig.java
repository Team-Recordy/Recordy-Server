package org.recordy.server.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.resolver.AccessTokenArgumentResolver;
import org.recordy.server.slack.interceptor.SlackInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AccessTokenArgumentResolver accessTokenArgumentResolver;
    private final SlackInterceptor slackInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accessTokenArgumentResolver);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(slackInterceptor)
//                .addPathPatterns("/api/v1/slack/interactive");
//    }
}
