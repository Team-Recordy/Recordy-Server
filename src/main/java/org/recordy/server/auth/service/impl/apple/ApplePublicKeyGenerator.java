package org.recordy.server.auth.service.impl.apple;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.security.PublicKey;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.stereotype.Component;
@Component
public class ApplePublicKeyGenerator {

    private static final String SIGN_ALGORITHM_HEADER_KEY = "alg";
    private static final String KEY_ID_HEADER_KEY = "kid";

    public PublicKey generatePublicKey(Map<String, String> headers, ApplePublicKeys applePublicKeys) {
        ApplePublicKey applePublicKey = applePublicKeys
                .getMatchingKey(headers.get(SIGN_ALGORITHM_HEADER_KEY), headers.get(KEY_ID_HEADER_KEY));

        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.e());

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(
                new BigInteger(1,nBytes),new BigInteger(1,eBytes)
        );

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.kty());
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthException(ErrorMessage.APPLE_UNABLE_TO_CREATE_PUBLIC_KEY);
        }
    }
}
