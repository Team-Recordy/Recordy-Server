package org.recordy.server.auth.service.impl.token;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static org.recordy.server.auth.service.dto.AuthTokenValidationResult.*;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    public AuthTokenServiceImpl(
            @Value("${auth.token.prefix}") String tokenPrefix,
            @Value("${auth.token.access.expiration}") long accessTokenExpiration,
            @Value("${auth.token.refresh.expiration}") long refreshTokenExpiration,
            @Value("${auth.token.access.type}") String ACCESS_TOKEN_TYPE,
            @Value("${auth.token.refresh.type}") String refreshTokenType,
            @Value("${auth.token.key.user_id}") String userIdKey,
            @Value("${auth.token.key.token_type}") String tokenTypeKey,
            AuthTokenGenerator tokenGenerator,
            AuthTokenParser tokenParser
    ) {
        this.tokenPrefix = tokenPrefix;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.accessTokenType = ACCESS_TOKEN_TYPE;
        this.refreshTokenType = refreshTokenType;
        this.userIdKey = userIdKey;
        this.tokenTypeKey = tokenTypeKey;
        this.tokenGenerator = tokenGenerator;
        this.tokenParser = tokenParser;
    }

    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final String accessTokenType;
    private final String refreshTokenType;
    private final String userIdKey;
    private final String tokenTypeKey;
    private final String tokenPrefix;

    private final AuthTokenGenerator tokenGenerator;
    private final AuthTokenParser tokenParser;

    @Override
    public AuthToken issueToken(long userId) {
        return new AuthToken(
                tokenGenerator.generate(
                        Map.of(userIdKey, userId, tokenTypeKey, accessTokenType),
                        accessTokenExpiration
                ),
                tokenGenerator.generate(
                        Map.of(userIdKey, userId, tokenTypeKey, refreshTokenType),
                        refreshTokenExpiration
                )
        );
    }

    @Override
    public AuthTokenValidationResult validateToken(String token) {
        try {
            tokenParser.getBody(token);

            return VALID_JWT;
        } catch (MalformedJwtException ex) {
            return INVALID_TOKEN;
        } catch (ExpiredJwtException ex) {
            return EXPIRED_TOKEN;
        } catch (UnsupportedJwtException ex) {
            return UNSUPPORTED_TOKEN;
        } catch (IllegalArgumentException ex) {
            return EMPTY_TOKEN;
        }
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(t -> t.startsWith(tokenPrefix))
                .map(t -> t.substring(tokenPrefix.length()))
                .orElseThrow(() -> new AuthException(ErrorMessage.INVALID_TOKEN));
    }

    @Override
    public long getUserIdFromToken(String token) {
        Claims claims = tokenParser.getBody(token);

        return Long.parseLong(claims.get(userIdKey).toString());
    }
}
