package org.recordy.server.auth.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.auth.security.UserAuthentication;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.recordy.server.auth.service.dto.AuthTokenValidationResult.*;

@RequiredArgsConstructor
@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    @Value("${auth.token.secret}")
    private String SECRET;
    @Value("${auth.token.access.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;
    @Value("${auth.token.refresh.expiration}")
    private long REFRESH_TOKEN_EXPIRATION;
    @Value("${auth.token.access.type}")
    private String ACCESS_TOKEN_TYPE;
    @Value("${auth.token.refresh.type}")
    private String REFRESH_TOKEN_TYPE;
    @Value("${auth.token.key.user_id}")
    private String USER_ID;
    @Value("${auth.token.key.token_type}")
    private String TOKEN_TYPE;
    @Value("${auth.token.prefix}")
    private String TOKEN_PREFIX;

    @Override
    public AuthToken issueToken(long userId) {
        UserAuthentication authentication = UserAuthentication.of(userId);

        return new AuthToken(
                generateToken(authentication, ACCESS_TOKEN_EXPIRATION, ACCESS_TOKEN_TYPE),
                generateToken(authentication, REFRESH_TOKEN_EXPIRATION, REFRESH_TOKEN_TYPE)
        );
    }

    @Override
    public AuthTokenValidationResult validateToken(String token) {
        try {
            getBody(token);

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
                .filter(t -> t.startsWith(TOKEN_PREFIX))
                .map(t -> t.replace(TOKEN_PREFIX.concat(" "), ""))
                .orElseThrow(() -> new AuthException(ErrorMessage.INVALID_ACCESS_TOKEN));
    }

    private String generateToken(UserAuthentication authentication, long expiration, String tokenType) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(generateClaims(Map.of(
                        USER_ID, authentication.getPrincipal(),
                        TOKEN_TYPE, tokenType
                )))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private static Claims generateClaims(Map<String, Object> values) {
        Claims claims = Jwts.claims();
        claims.putAll(values);

        return claims;
    }

    private Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(SECRET.getBytes());

        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }
}
