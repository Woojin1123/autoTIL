package com.woojin.autotil.common.config;

import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient githubRestClient(
            @Value("${GITHUB_CLIENT_SECRET}") String clientSecret,
            @Value("${GITHUB_CLIENT_ID}") String clientId
    ){
        String credentials = clientId + ":" + clientSecret;
        String encoded = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        return RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoded)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                // 4xx 에러: 토큰이 유효하지 않을 때
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> { throw new ApiException(ErrorCode.INVALID_TOKEN); }
                )
                // 5xx 에러: GitHub 서버 오류 등
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        (request, response) -> { throw new ApiException(ErrorCode.API_REQUEST_FAILED); }
                )
                .build();
    }

}
