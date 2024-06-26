package org.recordy.server.auth.service.impl.apple;

import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOAuthUserProvider {
    private final AppleFeignClient appleFeignClient;
    private final AppleIdentityTokenParser appleIdentityTokenParser;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;

    public String getApplePlatformId(String identityToken) {
        Map<String, String>  headers = appleIdentityTokenParser.parseHeaders(identityToken);
        ApplePublicKeys applePublicKeys = appleFeignClient.getApplePublicKeys();
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers,applePublicKeys);
        Claims claims = appleIdentityTokenParser.parsePubliKeyAndGetClaims(identityToken, publicKey);

        return claims.getSubject();
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new AuthException(ErrorMessage.APPLE_INVALID_IDENTITY_TOKEN_CLAIMS);
        }
    }
}
