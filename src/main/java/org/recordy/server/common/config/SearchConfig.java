package org.recordy.server.common.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SearchConfig {

    @Value("${search.api.host}")
    private String host;

    @Value("${search.api.port}")
    private int port;

    @Value("${search.api.key}")
    private String key;

    @Bean
    public RestClient restClient() {
        return RestClient
                .builder(new HttpHost(host, port, "https"))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader(HttpHeaders.AUTHORIZATION, "Apikey " + key)
                })
                .build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport() {
        return new RestClientTransport(restClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return new ElasticsearchClient(elasticsearchTransport());
    }
}
