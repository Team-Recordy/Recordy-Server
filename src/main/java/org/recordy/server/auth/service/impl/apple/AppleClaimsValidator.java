package org.recordy.server.auth.service.impl.apple;

import io.jsonwebtoken.Claims;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleClaimsValidator {
    
    private static final String NONCE_KEY = "nonce";

    @Value("${auth.oauth.apple.iss}")
    private String iss;

    @Value("${auth.oauth.apple.client-id}")
    private String clientId;

    @Value("${auth.oauth.apple.nonce}")
    private String nonce;

    public void validate(Claims claims) {
        this.nonce = EncryptUtils.encrypt(nonce);
        if (!(claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId) &&
                claims.get(NONCE_KEY, String.class).equals(nonce))){
            throw new AuthException(ErrorMessage.APPLE_INVALID_IDENTITY_TOKEN_CLAIMS);
        }
    }
}
