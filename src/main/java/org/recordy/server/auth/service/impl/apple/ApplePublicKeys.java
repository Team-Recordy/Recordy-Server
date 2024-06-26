package org.recordy.server.auth.service.impl.apple;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApplePublicKeys {
    private List<ApplePublicKey> keys;

    public ApplePublicKey getMatchesKey(String alg, String kid){
        return keys
                .stream()
                .filter(k -> k.alg().equals(alg) && k.kid().equals(kid))
                .findFirst()
                .orElseThrow( () -> new AuthException(ErrorMessage.APPLE_INVAILD_JWT));
    }
}
