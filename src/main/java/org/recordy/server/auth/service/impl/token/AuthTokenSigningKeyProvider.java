package org.recordy.server.auth.service.impl.token;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class AuthTokenSigningKeyProvider {

    private final String secret;

    public AuthTokenSigningKeyProvider(
            @Value("${auth.token.secret}")
            String secret
    ) {
        this.secret = secret;
    }

    public SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(secret.getBytes());

        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }
}
