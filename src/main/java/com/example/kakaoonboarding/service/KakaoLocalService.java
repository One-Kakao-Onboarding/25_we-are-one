package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.dto.Coordinates;
import com.example.kakaoonboarding.dto.kakao.KakaoLocalResponse;
import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
public class KakaoLocalService {

    private final WebClient webClient;
    private final String apiKey;

    public KakaoLocalService(@Value("${kakao.local.url}") String baseUrl,
                            @Value("${kakao.api.key}") String apiKey) {
        System.out.println("Kakao API Key: " + apiKey);
        System.out.println("Authorization Header: KakaoAK " + apiKey);

        HttpClient httpClient = HttpClient.create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG,
                        AdvancedByteBufFormat.TEXTUAL);

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Authorization", "KakaoAK " + apiKey)
                .build();
        this.apiKey = apiKey;
    }

    public Coordinates getCoordinates(String address) {
        System.out.println("========== KakaoLocalService ==========");
        System.out.println("주소 검색 시작: " + address);
        System.out.println("API 키: " + apiKey);
        System.out.println("Authorization 헤더: KakaoAK " + apiKey);

        KakaoLocalResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoLocalResponse.class)
                .block();

        System.out.println("API 응답 받음: " + (response != null ? "성공" : "실패"));
        if (response != null && response.getDocuments() != null) {
            System.out.println("검색 결과 개수: " + response.getDocuments().size());
        }

        if (response == null || response.getDocuments().isEmpty()) {
            throw new IllegalArgumentException("주소를 찾을 수 없습니다: " + address);
        }

        KakaoLocalResponse.Document document = response.getDocuments().get(0);
        System.out.println("좌표 변환 성공 - X: " + document.getX() + ", Y: " + document.getY());
        System.out.println("======================================");

        return new Coordinates(
                Double.parseDouble(document.getX()),
                Double.parseDouble(document.getY())
        );
    }
}
