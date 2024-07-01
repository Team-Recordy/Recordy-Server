package org.recordy.server.auth.repository;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;

public interface AuthRepository {

    // command
    Auth save(Auth auth);
    void delete(Auth auth);

    // query
    Auth findByRefeshToken(String refreshToken);
    Auth findByPlatformId(String platformId);
}
