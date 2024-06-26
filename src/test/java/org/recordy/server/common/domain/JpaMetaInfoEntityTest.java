package org.recordy.server.common.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.repository.impl.UserJpaRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class JpaMetaInfoEntityTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Test
    void JpaMetaInfoEntity를_상속하는_엔티티는_DB에_저장할_때_생성일과_수정일을_가진다() {
        // when
        UserEntity userEntity = userRepository.save(DomainFixture.createUserEntity());

        // then
        assertAll(
                () -> assertThat(userEntity.createdAt).isNotNull(),
                () -> assertThat(userEntity.updatedAt).isNotNull()
        );
    }
}