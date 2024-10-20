package org.recordy.server.record.service.impl;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.S3Service;
import org.recordy.server.record.domain.FileUrl;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecordServiceImpl implements RecordService {

    private final S3Service s3Service;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    @Override
    public Long create(RecordCreateRequest request, long uploaderId) {
        User user = userRepository.findById(uploaderId);
        Place place = placeRepository.findById(request.placeId());
        FileUrl fileUrl = s3Service.convertToCloudFrontUrl(request.fileUrl());

        return recordRepository.save(Record.create(RecordCreate.of(
                fileUrl,
                request.content(),
                user,
                place
        )));
    }

    @Transactional
    @Override
    public void delete(long userId, long recordId) {
        Record record = recordRepository.findById(recordId);

        if (!record.isUploader(userId)) {
            throw new RecordException(ErrorMessage.FORBIDDEN_DELETE_RECORD);
        }

        recordRepository.deleteById(recordId);
    }

    @Override
    public Slice<RecordGetResponse> getRecordsByPlaceId(long placeId, long userId, Long cursorId, int size) {
        return recordRepository.findAllByPlaceIdOrderByIdDesc(placeId, userId, cursorId, size);
    }

    @Override
    public Slice<RecordGetResponse> getRecentRecordsByUser(long otherUserId, long userId, Long cursorId, int size) {
        return recordRepository.findAllByUserIdOrderByIdDesc(otherUserId, userId, cursorId, size);
    }

    @Override
    public Slice<RecordGetResponse> getBookmarkedRecords(long userId, Long cursorId, int size) {
        return recordRepository.findAllByBookmarkOrderByIdDesc(userId, cursorId, size);
    }

    @Override
    public List<RecordGetResponse> getSubscribingRecords(long userId, int size) {
        List<Long> randomIds = getRandomSubscribingIds(userId, size);
        return recordRepository.findAllByIds(randomIds, userId);
    }

    private List<Long> getRandomSubscribingIds(long userId, int size) {
        List<Long> ids = recordRepository.findAllIdsBySubscribingUserId(userId);
        return getRandomSubList(ids, size);
    }

    @Override
    public List<RecordGetResponse> getRecords(long userId, int size) {
        List<Long> ids = getRandomIds(size);
        return recordRepository.findAllByIds(ids, userId);
    }

    private List<Long> getRandomIds(int size) {
        List<Long> ids = recordRepository.findAllIds();
        return getRandomSubList(ids, size);
    }

    private List<Long> getRandomSubList(List<Long> ids, int size) {
        Collections.shuffle(ids);
        return ids.subList(0, Math.min(size, ids.size()));
    }
}
