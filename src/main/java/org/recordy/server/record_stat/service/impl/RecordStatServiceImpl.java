package org.recordy.server.record_stat.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.domain.usecase.RecordInfo;
import org.recordy.server.record.domain.usecase.RecordInfoWithBookmark;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.recordy.server.record_stat.repository.BookmarkRepository;
import org.recordy.server.record_stat.service.RecordStatService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecordStatServiceImpl implements RecordStatService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final BookmarkRepository bookmarkRepository;

    @Override
    public Bookmark bookmark(long userId, long recordId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));

        return bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .record(record)
                .build());
    }

    @Override
    public void deleteBookmark(long userId, long recordId) {
        bookmarkRepository.delete(userId, recordId);
    }

    @Override
    public List<Boolean> findBookmarks(long userId, Slice<Record> records) {
        return records.getContent().stream()
                .map(record -> bookmarkRepository.findByUserIdAndRecordId(userId, record.getId()).isPresent())
                .collect(Collectors.toList());
    }

    @Override
    public Preference getPreference(long userId) {
        return Preference.of(userId, recordRepository.countAllByUserIdGroupByKeyword(userId));
    }


    @Override
    public Slice<RecordInfoWithBookmark> getBookmarkedRecordInfosWithBookmarks(long userId, long cursorId, int size) {
        Slice<Record> records = getBookmarkedRecords(userId, cursorId, size);
        List<Boolean> bookmarks = findBookmarks(userId, records);

        return records.map(record -> {
            int index = records.getContent().indexOf(record);
            Boolean isBookmarked = bookmarks.get(index);
            RecordInfo recordInfo = RecordInfo.from(record);
            return new RecordInfoWithBookmark(recordInfo, isBookmarked);
        });
    }

    public Slice<Record> getBookmarkedRecords(long userId, long cursorId, int size) {
        return bookmarkRepository.findAllByBookmarksOrderByIdDesc(userId, cursorId, PageRequest.ofSize(size))
                .map(Bookmark::getRecord);
    }
}
