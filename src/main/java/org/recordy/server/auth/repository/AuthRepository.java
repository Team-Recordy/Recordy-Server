package org.recordy.server.auth.repository;

import org.recordy.server.auth.domain.Auth;

public interface AuthRepository {

    Auth save(Auth auth);
}
