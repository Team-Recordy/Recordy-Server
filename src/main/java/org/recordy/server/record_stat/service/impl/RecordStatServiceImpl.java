package org.recordy.server.record_stat.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.recordy.server.record_stat.service.RecordStatService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecordStatServiceImpl implements RecordStatService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    @Override
    public Bookmark bookmark(long userId, long recordId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new UserException(ErrorMessage.RECORD_NOT_FOUND));




        return null;
    }

    @Override
    public Preference getPreference(long userId) {
        return null;
    }

    @Override
    public Slice<Record> getBookmarkedRecords(long userId, long cursorId, int size) {
        return null;
    }

    @Override
    public long getBookmarkCount(long recordId) {
        return 0;
    }
}
