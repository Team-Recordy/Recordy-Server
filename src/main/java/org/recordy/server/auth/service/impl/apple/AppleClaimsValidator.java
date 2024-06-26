package org.recordy.server.auth.service.impl.apple;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleClaimsValidator {
    private static final String NONCE_KEY = "nonce";

    @Value("${oauth.apple.iss}")
    private String iss;

    @Value("${oauth.apple.client-id")
    private String clientId;

    @Value("${oauth.apple.nonce}")
    private String nonce;

    public boolean isValid(Claims claims) {
        this.nonce = EncryptUtils.encrypt(nonce);
        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId) &&
                claims.get(NONCE_KEY, String.class).equals(nonce);
    }
}
