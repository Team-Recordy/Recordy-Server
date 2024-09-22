package org.recordy.server.user.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.dto.response.CursorBasePaginatedResponse;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.subscribe.domain.usecase.SubscribeCreate;
import org.recordy.server.subscribe.service.SubscribeService;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.recordy.server.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController implements UserApi {

    private final UserService userService;
    private final SubscribeService subscribeService;

    @Override
    @PostMapping("/follow/{followingId}")
    public ResponseEntity<Boolean> subscribe(
            @UserId Long userId,
            @PathVariable Long followingId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subscribeService.subscribe(new SubscribeCreate(userId, followingId)));
    }

    @Override
    @GetMapping("/following")
    public ResponseEntity<CursorBasePaginatedResponse<UserInfo>> getSubscribedUserInfos(
            @UserId Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CursorBasePaginatedResponse.of(
                        userService.getSubscribedUserInfos(userId, cursorId, size)
                ));
    }

    @Override
    @GetMapping("/follower")
    public ResponseEntity<CursorBasePaginatedResponse<UserInfo>> getSubscribingUserInfos(
            @UserId Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CursorBasePaginatedResponse.of(
                        userService.getSubscribingUserInfos(userId, cursorId, size)
                ));
    }

    @Override
    @GetMapping("/profile/{otherUserId}")
    public ResponseEntity<UserProfile> getUserInfosWithFollowing(
            @UserId Long userId,
            @PathVariable Long otherUserId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getProfile(userId, otherUserId));
    }
}
