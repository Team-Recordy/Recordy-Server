package org.recordy.server.user.domain.usecase;

import org.recordy.server.user.domain.User;

public record UserProfile(
        Long id,
        String nickname,
        String profileImageUrl,
        long recordCount,
        long followerCount,
        long followingCount,

        long bookmarkCount,
        boolean isFollowing
) {
    public static UserProfile of(User user, long recordCount, long followerCount, long followingCount, long bookmarkCount, boolean isFollowing) {
        return new UserProfile(
                user.getId(),
                user.getNickname(),
                user.getProfileImageUrl(),
                recordCount,
                followerCount,
                followingCount,
                bookmarkCount,
                isFollowing
        );
    }
}
