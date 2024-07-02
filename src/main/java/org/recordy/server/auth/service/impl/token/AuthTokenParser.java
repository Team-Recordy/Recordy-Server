package org.recordy.server.auth.service.impl.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthTokenParser {

    private final AuthTokenSigningKeyProvider signingKeyProvider;

    public Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKeyProvider.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
