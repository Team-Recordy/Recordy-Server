package org.recordy.server.record.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.service.dto.FileUrl;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    @Override
    public FileUrl save(File file) {
        return null;
    }
}
