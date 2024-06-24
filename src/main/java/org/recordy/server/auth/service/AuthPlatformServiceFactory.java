package org.recordy.server.auth.service;

import org.recordy.server.auth.domain.AuthPlatform;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthPlatformServiceFactory {

    private final Map<AuthPlatform.Type, AuthPlatformService> platformServices;

    public AuthPlatformServiceFactory(List<AuthPlatformService> platformServices) {
        this.platformServices = new HashMap<>();

        platformServices.forEach(
                platformService -> this.platformServices.put(platformService.getPlatformType(), platformService));
    }

    public AuthPlatformService getPlatformServiceFrom(AuthPlatform.Type platformType) {
        return platformServices.get(platformType);
    }
}
