package org.recordy.server.record.service;

import org.recordy.server.record.domain.File;
import org.recordy.server.record.controller.dto.FileUrl;

public interface FileService {

    // command
    FileUrl save(File file);
}
