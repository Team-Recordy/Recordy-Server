package org.recordy.server.record.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.common.util.RandomListUtils;
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
@Slf4j
public class RecordServiceImpl implements RecordService {

    private final S3Service s3Service;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    @Override
    public Long create(RecordCreateRequest request, long uploaderId) {
        try {
            User user = userRepository.findById(uploaderId);
            Place place = placeRepository.findById(request.placeId());
            FileUrl fileUrl = s3Service.convertToCloudFrontUrl(request.fileUrl());

            validateUserUploadHistory(user.getId());
            return recordRepository.save(Record.create(RecordCreate.of(
                    fileUrl,
                    request.content(),
                    request.exhibitionName(),
                    user,
                    place
            )));
        } catch (Exception e) {
            log.error("레코드 생성 중 오류 발생 - uploaderId: {}, placeId: {}, error: {}", 
                    uploaderId, request.placeId(), e.getMessage(), e);
            throw e;
        }
    }

    private void validateUserUploadHistory(Long userId) {
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime tomorrow = now.plusDays(1);

        if (recordRepository.countByUserIdAndCreatedAtBetween(userId, now, tomorrow) > 10) {
            throw new RecordException(ErrorMessage.RECORD_EXCEEDS_THRESHOLD);
        }
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
        return RandomListUtils.getRandomSubList(ids, size);
    }

    @Override
    public List<RecordGetResponse> getRecords(long userId, int size) {
        List<Long> ids = recordRepository.findAllIds(userId);
        ids = RandomListUtils.getRandomSubList(ids, size);

        return recordRepository.findAllByIds(ids, userId);
    }
}
