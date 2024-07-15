package org.recordy.server.subscribe.domain.usecase;

public record SubscribeCreate(
        long subscribingUserId,
        long subscribedUserId
) {
}
