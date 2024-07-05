package org.recordy.server.common.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
public class FFmpegConfig {

    @Value("${ffmpeg.main.location}")
    public String ffmpegLocation;

    @Value("${ffmpeg.probe.location}")
    private String ffprobeLocation;

    @Bean
    public FFmpeg ffMpeg() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(ffmpegLocation);
        return new FFmpeg(classPathResource.getURL().getPath());
    }

    @Bean
    public FFprobe ffProbe() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(ffprobeLocation);
        return new FFprobe(classPathResource.getURL().getPath());
    }
}

