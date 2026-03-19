package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.tbank.practicum.config.WeatherServiceProperties;
import ru.tbank.practicum.dto.external.DtoCoordinateRequest;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;

@Service
@RequiredArgsConstructor
public class WeatherClientService {

    private final RestClient restClient;

    private final WeatherServiceProperties weatherServiceProperties;

    public DtoWeatherResponse getWeatherByCoordinate(DtoCoordinateRequest request) {

        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(weatherServiceProperties.currentWeatherEndpoint())
                        .queryParam("lat", request.lat())
                        .queryParam("lon", request.lon())
                        .queryParam("units", "metric")
                        .queryParam("appid", weatherServiceProperties.cred().token())
                        .build())
                .retrieve()
                .body(DtoWeatherResponse.class);
    }
}
