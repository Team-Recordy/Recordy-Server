package org.recordy.server.auth.repository;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;

import java.util.Optional;

public interface AuthRepository {

    // command
    Auth save(Auth auth);
    void delete(Auth auth);

    // query
    Optional<Auth> findByRefreshToken(String refreshToken);
    Optional<Auth> findByPlatformId(String platformId);
}
