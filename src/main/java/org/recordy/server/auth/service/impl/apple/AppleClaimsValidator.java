package org.recordy.server.auth.service.impl.apple;

import io.jsonwebtoken.Claims;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleClaimsValidator {
    
    private static final String NONCE_KEY = "nonce";
    private String iss;
    private String clientId;

    public AppleClaimsValidator(
            @Value("${auth.oauth.apple.iss}") String iss,
            @Value("${auth.oauth.apple.client-id}") String clientId
    ) {
        this.iss = iss;
        this.clientId = clientId;
    }

    public void validate(Claims claims) {
        if (!(claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId))){
            throw new AuthException(ErrorMessage.APPLE_INVALID_IDENTITY_TOKEN_CLAIMS);
        }
    }
}
