package org.recordy.server.mock.subscribe;

import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeSubscribeRepository implements SubscribeRepository {

    public long subscribeAutoIncrementId = 1L;
    private final Map<Long, Subscribe> subscribes = new HashMap<>();

    @Override
    public void save(Subscribe subscribe) {
        Subscribe realSubscribe = Subscribe.builder()
                .id(subscribeAutoIncrementId)
                .subscribingUser(subscribe.getSubscribingUser())
                .subscribedUser(subscribe.getSubscribedUser())
                .build();
        subscribes.put(subscribeAutoIncrementId++, realSubscribe);
    }

    @Override
    public void delete(long subscribingUserId, long subscribedUserId) {
        subscribes.values()
                .removeIf(subscribe ->
                        subscribe.getSubscribingUser().getId() == subscribingUserId &&
                                subscribe.getSubscribedUser().getId() == subscribedUserId
                );
    }

    @Override
    public void deleteByUserId(long userId) {
        subscribes.values()
                .removeIf(subscribe ->
                        subscribe.getSubscribingUser().getId() == userId ||
                                subscribe.getSubscribedUser().getId() == userId
                );
    }

    @Override
    public Slice<Subscribe> findAllBySubscribingUserId(long subscribingUserId, long cursor, Pageable pageable) {
        List<Subscribe> content = subscribes.values().stream()
                .filter(subscribe ->
                        subscribe.getSubscribingUser().getId() == subscribingUserId &&
                                subscribe.getSubscribedUser().getId() < cursor
                )
                .sorted(Comparator.comparing(Subscribe::getId).reversed())
                .toList();

        if (content.size() <= pageable.getPageSize()) {
            return new SliceImpl<>(content, pageable, false);
        }

        return new SliceImpl<>(content, pageable, true);
    }

    @Override
    public Slice<Subscribe> findAllBySubscribedUserId(long subscribedUserId, long cursor, Pageable pageable) {
        List<Subscribe> content = subscribes.values().stream()
                .filter(subscribe ->
                        subscribe.getSubscribingUser().getId() == subscribedUserId &&
                                subscribe.getSubscribedUser().getId() < cursor
                )
                .sorted(Comparator.comparing(Subscribe::getId).reversed())
                .toList();

        if (content.size() <= pageable.getPageSize()) {
            return new SliceImpl<>(content, pageable, false);
        }

        return new SliceImpl<>(content, pageable, true);
    }

    @Override
    public boolean existsBySubscribingUserIdAndSubscribedUserId(long subscribingUserId, long subscribedUserId) {
        return subscribes.values().stream()
                .anyMatch(subscribe ->
                        subscribe.getSubscribingUser().getId() == subscribingUserId &&
                        subscribe.getSubscribedUser().getId() == subscribedUserId
                );
    }

    @Override
    public long countSubscribingUsers(long subscribedUserId) {
        return subscribes.values().stream()
                .filter(subscribe -> subscribe.getSubscribedUser().getId() == subscribedUserId)
                .count();
    }

    @Override
    public long countSubscribedUsers(long subscribingUserId) {
        return subscribes.values().stream()
                .filter(subscribe -> subscribe.getSubscribingUser().getId() == subscribingUserId)
                .count();
    }
}
