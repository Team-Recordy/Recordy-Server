package org.recordy.server.auth.service.impl.apple;

import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthPlatform.Type;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.auth.service.AuthPlatformService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthApplePlatformServiceImpl implements AuthPlatformService {
    private final AppleFeignClient appleFeignClient;
    private final AppleIdentityTokenParser appleIdentityTokenParser;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;

    @Override
    public AuthPlatform getPlatform(AuthSignIn authSignIn) {
        String identityToken = authSignIn.platformToken();
        Map<String, String> headers = appleIdentityTokenParser.parseHeaders(identityToken);
        ApplePublicKeys applePublicKeys = appleFeignClient.getApplePublicKeys();
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers,applePublicKeys);
        Claims claims = appleIdentityTokenParser.parsePubliKeyAndGetClaims(identityToken, publicKey);
        validateClaims(claims);
        return new AuthPlatform(claims.getSubject(), Type.APPLE);
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new AuthException(ErrorMessage.APPLE_INVALID_IDENTITY_TOKEN_CLAIMS);
        }
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.APPLE;
    }
}
