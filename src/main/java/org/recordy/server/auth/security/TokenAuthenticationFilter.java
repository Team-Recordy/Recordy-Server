package org.recordy.server.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.security.handler.AuthFilterExceptionHandler;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.recordy.server.auth.service.dto.AuthTokenValidationResult.VALID_JWT;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final String[] authFreeApis;
    private final String[] authDevApis;

    private final AuthTokenService authTokenService;
    private final AuthFilterExceptionHandler exceptionHandler;

    public TokenAuthenticationFilter(
            @Value("#{'${auth.apis.free}'.split(',')}") String[] authFreeApis,
            @Value("#{'${auth.apis.dev}'.split(',')}") String[] authDevApis,
            AuthTokenService authTokenService,
            AuthFilterExceptionHandler exceptionHandler
    ) {
        this.authFreeApis = authFreeApis;
        this.authDevApis = authDevApis;
        this.authTokenService = authTokenService;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher uriMatcher = new AntPathMatcher();
        String uri = request.getRequestURI();

        return Stream.concat(Arrays.stream(authFreeApis), Arrays.stream(authDevApis))
                .anyMatch(api -> uriMatcher.match(api, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = authTokenService.getTokenFromRequest(request);
            validateToken(token);
            setUserIntoContext(token, request);
        } catch (AuthException e) {
            exceptionHandler.handleFilterException(e, response);
        }

        filterChain.doFilter(request, response);
    }

    private void validateToken(String token) {

        AuthTokenValidationResult validationResult = authTokenService.validateToken(token);

        if (validationResult == VALID_JWT)
            return;
        if (validationResult == AuthTokenValidationResult.EXPIRED_TOKEN)
            throw new AuthException(ErrorMessage.EXPIRED_TOKEN);

        throw new AuthException(ErrorMessage.INVALID_TOKEN_VALUE);
    }

    private void setUserIntoContext(String token, HttpServletRequest request) {
        UserAuthentication authentication = UserAuthentication.of(authTokenService.getUserIdFromToken(token));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
