package org.recordy.server.bookmark.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.bookmark.service.BookmarkService;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    @Override
    public boolean bookmark(long userId, long recordId) {
        if (bookmarkRepository.existsByUserIdAndRecordId(userId, recordId)) {
            bookmarkRepository.delete(userId, recordId);
            return false;
        }

        User user = userRepository.findById(userId);
        Record record = recordRepository.findById(recordId);

        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .record(record)
                .build());

        return true;
    }

    @Override
    public Long countBookmarks(long userId) {
        return bookmarkRepository.countByUserId(userId);
    }
}

