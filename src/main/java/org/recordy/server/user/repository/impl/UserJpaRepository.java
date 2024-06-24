package org.recordy.server.user.repository.impl;

import org.recordy.server.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByPlatformId(String platformId);
}
