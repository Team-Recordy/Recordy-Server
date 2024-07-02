package org.recordy.server.auth.service.impl.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthTokenGenerator {

    private final AuthTokenSigningKeyProvider signingKeyProvider;

    public String generate(Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(generateClaims(claims))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKeyProvider.getSigningKey())
                .compact();
    }

    private static Claims generateClaims(Map<String, Object> values) {
        Claims claims = Jwts.claims();
        claims.putAll(values);

        return claims;
    }
}
