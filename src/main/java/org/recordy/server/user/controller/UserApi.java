package org.recordy.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.common.dto.response.CursorBasePaginatedResponse;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.user.controller.dto.request.UserUpdateRequest;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "유저 관련 API")
public interface UserApi {

    @Operation(
            summary = "사용자 수정 API",
            description = "닉네임 또는 프로필 이미지 url을 수정할 수 있는 API입니다. ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공"
                    )
            }
    )
    public ResponseEntity<Void> update(
            Long userId,
            UserUpdateRequest request
    );

    @Operation(
            summary = "팔로우 및 언팔로우 API",
            description = "특정 유저를 팔로우하거나 언팔로우하는 API입니다. ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공"
                    )
            }
    )
    public ResponseEntity<Boolean> subscribe(
            @UserId Long userId,
            @PathVariable Long subscribedUserId
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "팔로잉 리스트 조회 API",
            description = "사용자가 팔로우하고 있는 사람 리스트를 조회하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UserInfo.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<CursorBasePaginatedResponse<UserInfo>> getSubscribedUserInfos(
            @UserId Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "팔로우 리스트 조회 API",
            description = "사용자를 팔로우하고 있는 사람 리스트를 조회하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UserInfo.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<CursorBasePaginatedResponse<UserInfo>> getSubscribingUserInfos(
            @UserId Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "팔로우 리스트 조회 API",
            description = "사용자를 팔로우하고 있는 사람 리스트를 조회하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공"
                    )
            }
    )
    public ResponseEntity<UserProfile> getUserInfosWithFollowing(
            @UserId Long userId,
            @PathVariable Long otherUserId
    );
}