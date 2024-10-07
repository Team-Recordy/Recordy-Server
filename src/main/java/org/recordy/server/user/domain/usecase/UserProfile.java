package org.recordy.server.user.domain.usecase;

public record UserProfile(
        Long id,
        String nickname,
        String profileImageUrl,
        long recordCount,
        long followerCount,
        long followingCount,
        boolean isFollowing
) {
}
