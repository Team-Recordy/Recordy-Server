package org.recordy.server.subscribe.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.subscribe.controller.dto.response.UserInfoWithFollowing;
import org.recordy.server.subscribe.domain.usecase.SubscribeCreate;
import org.recordy.server.subscribe.service.SubscribeService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class SubscribeController implements SubscribeApi{
    private final SubscribeService subscribeService;

    @Override
    @PostMapping("/follow/{followingId}")
    public ResponseEntity<Void> subscribe(
            @UserId Long userId,
            @PathVariable Long followingId
    ) {
        subscribeService.subscribe(new SubscribeCreate(userId, followingId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @DeleteMapping("/unfollow/{followingId}")
    public ResponseEntity<Void> unsubscribe(
            @UserId Long userId,
            @PathVariable Long followingId
    ) {
        subscribeService.unsubscribe(userId, followingId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    @GetMapping("/following")
    public ResponseEntity<Slice<UserInfo>> getSubscribedUserInfos(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        System.out.println(subscribeService.getSubscribedUsers(userId, cursorId, size));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserInfo.of(subscribeService.getSubscribedUsers(userId, cursorId, size)));
    }

    @Override
    @GetMapping("/follower")
    public ResponseEntity<Slice<UserInfoWithFollowing>> getSubscribingUserInfos(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<User> users = subscribeService.getSubscribingUsers(userId, cursorId, size);
        List<Boolean> following = subscribeService.findSubscribes(userId, users);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserInfoWithFollowing.of(users, following));
    }
}
