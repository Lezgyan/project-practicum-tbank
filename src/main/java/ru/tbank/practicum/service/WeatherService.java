package ru.tbank.practicum.service;

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

import java.util.List;

@Service
public class WeatherService {
    private final RestClient restClient;

    private final WeatherRepository weatherRepository;

    private final MapperCoordinate mapperCoordinate;

    private final MapperWeather mapperWeather;

//    @Value("${app.cred.token}")
    private final String token = "";

    public WeatherService(RestClient restClient,
                          WeatherRepository weaherRepository,
                          MapperCoordinate mapperCoordinate,
                          MapperWeather mapperWeather) {
        this.restClient = restClient;
        this.weatherRepository = weaherRepository;
        this.mapperCoordinate = mapperCoordinate;
        this.mapperWeather = mapperWeather;
    }

    public DtoWeather getWeatherByCoordinate(DtoCoordinate dtoCoordinate) {
        EntityCoordinate entityCoordinate = mapperCoordinate.mapToEntity(dtoCoordinate);

        DtoWeatherResponse weatherResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("lat", entityCoordinate.lat())
                        .queryParam("lon", entityCoordinate.lon())
                        .queryParam("units", "metric")
                        .queryParam("appid", token)
                        .build())
                .retrieve()
                .body(DtoWeatherResponse.class);

        //weaherRepository.save();
        return mapperWeather.mapToDtoWeather(weatherResponse);
    }


    public List<DtoWeather> getForecasts() {
        List<EntityWeather> entities = weatherRepository.getAll();
        return entities.stream()
                .map(mapperWeather::mapToDtoWeather)
                .toList();
    }

    public DtoWeather getForecast(Long id) {
        EntityWeather entity = weatherRepository.getById(id);
        return mapperWeather.mapToDtoWeather(entity);
    }

    public void deleteForecast(Long id) {
        weatherRepository.deleteById(id);
    }

}