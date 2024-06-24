package org.recordy.server.mock.auth;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.repository.AuthRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeAuthRepository implements AuthRepository {

    private final Map<String, Auth> auths = new HashMap<>();

    @Override
    public Auth save(Auth auth) {
        auths.put(auth.getPlatform().getId(), auth);

        return auth;
    }
}
