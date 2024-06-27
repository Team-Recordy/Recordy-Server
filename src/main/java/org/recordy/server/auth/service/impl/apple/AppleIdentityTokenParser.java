package org.recordy.server.auth.service.impl.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
public class AppleIdentityTokenParser {

    private static final String IDENTITY_TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Map<String, String> parseHeaders(String identityToken) {
        try {
            String encodedHeader = identityToken.split(IDENTITY_TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
            String decodedHeader = new String(Base64.getUrlDecoder().decode(encodedHeader));
            return OBJECT_MAPPER.readValue(decodedHeader, Map.class);
        } catch (JsonProcessingException | ArrayIndexOutOfBoundsException e) {
            throw new AuthException(ErrorMessage.APPLE_INVALID_IDENTITY_TOKEN);
        }
    }

    public Claims parsePubliKeyAndGetClaims(String identityToken, PublicKey publicKey) {
        try {
            return getJwtParser(publicKey)
                    .parseClaimsJws(identityToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorMessage.APPLE_EXPIRED_IDENTITY_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e){
            throw new AuthException(ErrorMessage.APPLE_INVALID_IDENTITY_TOKEN_VALUE);
        }
    }

    private JwtParser getJwtParser(Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

}
