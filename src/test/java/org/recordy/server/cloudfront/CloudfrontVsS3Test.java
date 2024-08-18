package org.recordy.server.cloudfront;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CloudfrontVsS3Test {

    private RestTemplate restTemplate;
    private static final String S3_DOMAIN = "recordy-bucket.s3.ap-northeast-2.amazonaws.com";
    private static final String CLOUDFRONT_DOMAIN = "d2p19guzt9trnp.cloudfront.net";
    private String[] urls = {
            "https://{domain}/thumbnails/009bdfac-d02b-4f85-bf6d-c08b36e36ea3.jpeg",
            "https://{domain}/thumbnails/0119d0e3-75e7-4e1b-8598-36cf65db89bd.jpeg",
            "https://{domain}/thumbnails/02075a21-3ad2-42d1-8bbd-f900c561170c.jpg",
            "https://{domain}/thumbnails/0272c691-d0e8-4ec5-b616-6c912e049c05.jpeg",
            "https://{domain}/thumbnails/036cb125-97de-4a04-b6e1-c00ab8e8ed73.jpeg",
            "https://{domain}/thumbnails/03b95e00-6c25-4cb8-9a2d-1746282021cb.jpeg",
            "https://{domain}/thumbnails/03f1d3cf-a100-4e33-b3f1-10670508aad0.jpeg",
            "https://{domain}/thumbnails/04a9c879-9eff-4bcb-a0b7-8cbf2d2f5fed.jpeg",
            "https://{domain}/thumbnails/04aa62ce-efc9-4b53-ac55-c02c62f7993a.jpeg",
            "https://{domain}/thumbnails/07036dcf-c580-4493-93ad-01a4bf4a6395.jpeg"
    };

    int numberOfConcurrentUsers = 3;
    ExecutorService executorService;

    AtomicLong latency;
    AtomicInteger cacheHits;


    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        executorService = Executors.newFixedThreadPool(numberOfConcurrentUsers);

        latency = new AtomicLong(0);
        cacheHits = new AtomicInteger(0);
    }

    @Test
    void S3_객체에_대해_정해진_수만큼_동시다발적으로_요청한다() throws Exception {
        executeConcurrently(S3_DOMAIN);
    }

    @Test
    void Cloudfront_객체에_대해_정해진_수만큼_동시다발적으로_요청한다() throws Exception {
        executeConcurrently(CLOUDFRONT_DOMAIN);
    }

    private void executeConcurrently(String domain) throws Exception {
        for (int i = 0; i < numberOfConcurrentUsers; i++)
            executorService.execute(() -> sendHttpRequest(domain));

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        printResult();
    }

    private void sendHttpRequest(String domain) {
        for (String url : urls) {
            String replacedUrl = url.replace("{domain}", domain);

            long start = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.exchange(replacedUrl, HttpMethod.GET, null, String.class);
            long end = System.currentTimeMillis();

            latency.addAndGet(end - start);
            incrementCacheHits(response);
        }
    }

    private void incrementCacheHits(ResponseEntity<String> response) {
        String cacheHeader = response.getHeaders().getFirst("X-Cache");

        if (Objects.nonNull(cacheHeader) && cacheHeader.startsWith("Hit from cloudfront"))
            cacheHits.incrementAndGet();
    }

    private void printResult() {
        System.out.println("latency = " + latency.get() + "ms");
        System.out.println("cacheHits = " + cacheHits);
    }
}
