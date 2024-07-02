package org.recordy.server.auth.repository;

import org.recordy.server.auth.domain.Auth;

import java.util.Optional;

public interface AuthRepository {

    // command
    Auth save(Auth auth);
    void delete(Auth auth);

    // query
    Optional<Auth> findByPlatformId(String platformId);
}
