package org.recordy.server.record.service;

import org.recordy.server.record.service.dto.File;
import org.recordy.server.record.service.dto.FileUrl;

public interface FileService {

    // command
    FileUrl save(File file);
}
