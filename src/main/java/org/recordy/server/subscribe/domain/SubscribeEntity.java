package org.recordy.server.subscribe.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.user.domain.UserEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "subscribes")
@Entity
public class SubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribing_user_id")
    private UserEntity subscribingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribed_user_id")
    private UserEntity subscribedUser;

    @Builder
    public SubscribeEntity(Long id, UserEntity subscribingUser, UserEntity subscribedUser) {
        this.id = id;
        this.subscribingUser = subscribingUser;
        this.subscribedUser = subscribedUser;
    }

    public static SubscribeEntity from(Subscribe subscribe) {
        return SubscribeEntity.builder()
                .id(subscribe.getId())
                .subscribingUser(UserEntity.from(subscribe.getSubscribingUser()))
                .subscribedUser(UserEntity.from(subscribe.getSubscribedUser()))
                .build();
    }

    public Subscribe toDomain() {
        return Subscribe.builder()
                .id(id)
                .subscribingUser(subscribingUser.toDomain())
                .subscribedUser(subscribedUser.toDomain())
                .build();
    }
}
