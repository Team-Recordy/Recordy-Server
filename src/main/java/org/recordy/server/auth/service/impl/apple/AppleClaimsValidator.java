package org.recordy.server.auth.service.impl.apple;

import io.jsonwebtoken.Claims;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleClaimsValidator {
    
    private static final String NONCE_KEY = "nonce";
    private String iss;
    private String clientId;
    private String nonce;

    public AppleClaimsValidator(
            @Value("${auth.oauth.apple.iss}") String iss,
            @Value("${auth.oauth.apple.client-id}") String clientId,
            @Value("${auth.oauth.apple.nonce}") String nonce
    ) {
        this.iss = iss;
        this.clientId = clientId;
        this.nonce = EncryptUtils.encrypt(nonce);
    }

    public void validate(Claims claims) {
        this.nonce = EncryptUtils.encrypt(nonce);
        if (!(claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId) &&
                claims.get(NONCE_KEY, String.class).equals(nonce))){
            throw new AuthException(ErrorMessage.APPLE_INVALID_IDENTITY_TOKEN_CLAIMS);
        }
    }
}
