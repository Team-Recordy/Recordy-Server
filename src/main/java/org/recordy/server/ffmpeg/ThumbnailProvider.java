package org.recordy.server.ffmpeg;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class ThumbnailProvider {

    private final FFmpeg ffMpeg;
    private final FFprobe ffProbe;

    public File extractThumbnail(MultipartFile videoFile) throws Exception {
        File outputThumbnailFile = File.createTempFile("temp_", ".jpg");

        Path tempFilePath = outputThumbnailFile.toPath();
        Files.copy(videoFile.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

        File thumbnailOutputFile = File.createTempFile("thumbnail_", ".jpg");
        thumbnailOutputFile.deleteOnExit();

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(outputThumbnailFile.toString())
                .overrideOutputFiles(true)
                .addOutput(thumbnailOutputFile.getAbsolutePath())
                .setFrames(1)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffMpeg, ffProbe);
        executor.createJob(builder).run();

        return thumbnailOutputFile;
    }
}
