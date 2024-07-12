package org.recordy.server.record_stat.repository.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.BookmarkEntity;
import org.recordy.server.record_stat.repository.BookmarkRepository;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.impl.UserJpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final BookmarkQueryDslRepository bookmarkQueryDslRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Bookmark save(Bookmark bookmark) {
        return bookmarkJpaRepository.save(BookmarkEntity.from(bookmark))
                .toDomain();
    }


    @Override
    public Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                        .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        return bookmarkQueryDslRepository.findAllByUserOrderByIdDesc(userEntity, cursor, pageable)
                .map(BookmarkEntity::toDomain);
    }

    @Override
    public void deleteById(long bookmarkId) {
            bookmarkJpaRepository.deleteById(bookmarkId);
    }

    @Override
    public Optional<Bookmark> findByUserIdAndRecordId(long userId, long recordId) {
        return bookmarkJpaRepository.findByUser_IdAndRecord_Id(userId, recordId)
                .map(BookmarkEntity::toDomain);
    }
}
