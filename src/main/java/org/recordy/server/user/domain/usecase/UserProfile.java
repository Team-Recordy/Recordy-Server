package org.recordy.server.user.domain.usecase;

import org.recordy.server.user.domain.User;

public record UserProfile(
        Long id,
        String nickname,
        String profileImageUrl,
        long recordCount,
        long followerCount,
        long followingCount
) {
    public static UserProfile of(User user, long recordCount, long followerCount, long followingCount) {
        return new UserProfile(
                user.getId(),
                user.getNickname(),
                user.getProfileImageUrl(),
                recordCount,
                followerCount,
                followingCount
        );
    }
}
