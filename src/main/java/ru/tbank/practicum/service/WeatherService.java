package ru.tbank.practicum.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
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

    private final WeatherRepository weatherRepository;

    private final WeatherClientService weatherClientService;

    private final MapperCoordinate mapperCoordinate;

    private final MapperWeather mapperWeather;

    public WeatherService(
            WeatherRepository weatherRepository,
            WeatherClientService weatherClientService,
            MapperCoordinate mapperCoordinate,
            MapperWeather mapperWeather) {
        this.weatherRepository = weatherRepository;
        this.weatherClientService = weatherClientService;
        this.mapperCoordinate = mapperCoordinate;
        this.mapperWeather = mapperWeather;
    }

    public DtoWeather getWeatherByCoordinate(DtoCoordinate dtoCoordinate) {
        EntityCoordinate entityCoordinate = mapperCoordinate.mapToEntityCoordinate(dtoCoordinate);

        DtoWeatherResponse weatherResponse = weatherClientService.getWeatherByCoordinate(entityCoordinate);

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
