package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.dto.Coordinates;
import com.example.kakaoonboarding.dto.kakao.KakaoMobilityResponse;
import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
public class KakaoMobilityService {

    private final WebClient webClient;
    private final String apiKey;

    public KakaoMobilityService(@Value("${kakao.mobility.url}") String baseUrl,
                               @Value("${kakao.api.key}") String apiKey) {
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

    public KakaoMobilityResponse.Summary getDistance(Coordinates origin, Coordinates destination) {
        System.out.println("========== KakaoMobilityService ==========");
        String originParam = origin.getX() + "," + origin.getY();
        String destinationParam = destination.getX() + "," + destination.getY();
        System.out.println("출발지 좌표: " + originParam);
        System.out.println("목적지 좌표: " + destinationParam);
        System.out.println("API 키: " + apiKey);

        KakaoMobilityResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/directions")
                        .queryParam("origin", originParam)
                        .queryParam("destination", destinationParam)
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoMobilityResponse.class)
                .block();

        System.out.println("API 응답 받음: " + (response != null ? "성공" : "실패"));
        if (response != null && response.getRoutes() != null) {
            System.out.println("경로 개수: " + response.getRoutes().size());
        }

        if (response == null || response.getRoutes().isEmpty()) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다");
        }

        KakaoMobilityResponse.Summary summary = response.getRoutes().get(0).getSummary();
        System.out.println("거리: " + summary.getDistance() + "m, 시간: " + summary.getDuration() + "초");
        System.out.println("=========================================");

        return summary;
    }
}
