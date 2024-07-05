package org.recordy.server.ffmpeg;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class ThumbnailProviderTest {

    @Autowired
    private ThumbnailProvider thumbnailProvider;

    @Test
    public void testExtractThumbnail() throws Exception {
        // Given: A sample video file from the classpath
        ClassPathResource videoResource = new ClassPathResource("/video/sample-video.mp4");
        MultipartFile videoFile = new MockMultipartFile(
                "sample-video.mp4",
                videoResource.getInputStream()
        );

        // When: Extracting thumbnail from the video file
        File thumbnail = thumbnailProvider.extractThumbnail(videoFile);

        // Then: Verify the thumbnail file exists and is not empty
        assertThat(thumbnail).isNotNull();
        assertThat(thumbnail.exists()).isTrue();
        assertThat(thumbnail.length()).isGreaterThan(0);

        // Print the thumbnail file path
        //System.out.println("Thumbnail file path: " + thumbnail.getAbsolutePath());

        // Clean up
        thumbnail.delete();
    }
}