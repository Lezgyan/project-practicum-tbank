package ru.tbank.practicum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.tbank.practicum.dto.external.DtoCoordinateRequest;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;

@Service
public class WeatherClientService {

    private final RestClient restClient;

    @Value("${app.cred.token}")
    private String token;

    @Value("${app.path}")
    private String path;

    public WeatherClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    public DtoWeatherResponse getWeatherByCoordinate(DtoCoordinateRequest request) {

        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("lat", request.lat())
                        .queryParam("lon", request.lon())
                        .queryParam("units", "metric")
                        .queryParam("appid", token)
                        .build())
                .retrieve()
                .body(DtoWeatherResponse.class);
    }
}
