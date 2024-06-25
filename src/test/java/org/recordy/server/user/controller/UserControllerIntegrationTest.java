package org.recordy.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/user-service-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void signIn을_통해_사용자는_카카오_플랫폼_토큰을_통해_가입_이후_토큰을_반환받을_수_있다() throws Exception {
        // given
        String request = objectMapper.writeValueAsString(new UserSignInRequest(AuthPlatform.Type.KAKAO));

        // when
        mockMvc.perform(post("/api/v1/users/signIn")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + "token")
                        .content(request))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.isSignedUp").value(false));
    }

    @Test
    void signIn을_통해_사용자는_애플_플랫폼_토큰을_통해_가입_이후_토큰을_반환받을_수_있다() throws Exception {
        // given
        String request = objectMapper.writeValueAsString(new UserSignInRequest(AuthPlatform.Type.APPLE));

        // when
        mockMvc.perform(post("/api/v1/users/signIn")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + "token")
                        .content(request))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.isSignedUp").value(false));
    }

    @Test
    void Authorization_헤더가_비어_있을_경우_400을_반환한다() throws Exception {
        // given
        String request = objectMapper.writeValueAsString(new UserSignInRequest(AuthPlatform.Type.KAKAO));

        // when
        mockMvc.perform(post("/api/v1/users/signIn")
                        .contentType("application/json")
                        .content(request))

                // then
                .andExpect(status().isBadRequest());
    }
}