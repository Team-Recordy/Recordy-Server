package org.recordy.server.common.config;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.TokenAuthenticationFilter;
import org.recordy.server.auth.security.handler.UndefinedAccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("#{'${auth.apis.free}'.split(',')}")
    private String[] authFreeApis;
    @Value("#{'${auth.apis.dev}'.split(',')}")
    private String[] authDevApis;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final UndefinedAccessHandler undefinedAccessHandler;

    @Bean
    @Profile("local")
    public SecurityFilterChain localHttpSecurity(HttpSecurity http) throws Exception {
        permitDevelopApis(http);
        setHttp(http);

        return http.build();
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain stageHttpSecurity(HttpSecurity http) throws Exception {
        permitDevelopApis(http);
        setHttp(http);

        return http.build();
    }

    private void permitDevelopApis(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                requests.requestMatchers(authDevApis).permitAll()
        );
    }

    private void setHttp(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exception -> exception.accessDeniedHandler(undefinedAccessHandler))
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(authFreeApis).permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().denyAll()
                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
