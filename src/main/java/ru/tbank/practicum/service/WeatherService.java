package ru.tbank.practicum.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.entity.EntityCoordinate;
import ru.tbank.practicum.entity.EntityWeather;
import ru.tbank.practicum.mapper.MapperCoordinate;
import ru.tbank.practicum.mapper.MapperWeather;
import ru.tbank.practicum.repository.WeatherRepository;

@Service
public class WeatherService {
    private final RestClient restClient;

    private final WeatherRepository weatherRepository;

    private final MapperCoordinate mapperCoordinate;

    private final MapperWeather mapperWeather;

    @Value("${app.cred.token}")
    private String token;

    @Value("${app.path}")
    private String path;

    public WeatherService(
            RestClient restClient,
            WeatherRepository weatherRepository,
            MapperCoordinate mapperCoordinate,
            MapperWeather mapperWeather) {
        this.restClient = restClient;
        this.weatherRepository = weatherRepository;
        this.mapperCoordinate = mapperCoordinate;
        this.mapperWeather = mapperWeather;
    }

    public DtoWeather getWeatherByCoordinate(DtoCoordinate dtoCoordinate) {
        EntityCoordinate entityCoordinate = mapperCoordinate.mapToEntityCoordinate(dtoCoordinate);

        DtoWeatherResponse weatherResponse = restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("lat", entityCoordinate.lat())
                        .queryParam("lon", entityCoordinate.lon())
                        .queryParam("units", "metric")
                        .queryParam("appid", token)
                        .build())
                .retrieve()
                .body(DtoWeatherResponse.class);

        weatherRepository.save(mapperWeather.mapToEntityWeather(weatherResponse));

        return mapperWeather.mapToDtoWeather(weatherResponse);
    }

    public List<DtoWeather> getForecasts() {
        List<EntityWeather> entities = weatherRepository.getAll();
        return new ArrayList<>(List.of(new DtoWeather(1L, 12.0, "testing")));
        //        return entities.stream()
        //                .map(mapperWeather::mapToDtoWeather)
        //                .toList();
    }

    public DtoWeather getForecast(Long id) {
        EntityWeather entity = weatherRepository.getById(id);
        return mapperWeather.mapToDtoWeather(entity);
    }

    public void deleteForecast(Long id) {
        weatherRepository.deleteById(id);
    }
}
