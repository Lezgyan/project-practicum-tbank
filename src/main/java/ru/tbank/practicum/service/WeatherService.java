package ru.tbank.practicum.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.external.DtoCoordinateRequest;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.internal.DtoCoordinate;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.entity.EntityWeather;
import ru.tbank.practicum.mapper.MapperWeather;
import ru.tbank.practicum.repository.WeatherRepository;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    private final WeatherClientService weatherClientService;

    private final MapperWeather mapperWeather;

    public WeatherService(
            WeatherRepository weatherRepository,
            WeatherClientService weatherClientService,
            MapperWeather mapperWeather) {
        this.weatherRepository = weatherRepository;
        this.weatherClientService = weatherClientService;
        this.mapperWeather = mapperWeather;
    }

    public DtoWeather getWeatherByCoordinate(DtoCoordinate dtoCoordinate) {
        DtoCoordinateRequest request = new DtoCoordinateRequest(dtoCoordinate.lat(), dtoCoordinate.lon());

        DtoWeatherResponse weatherResponse = weatherClientService.getWeatherByCoordinate(request);

        weatherRepository.save(mapperWeather.mapToEntityWeather(weatherResponse));

        return mapperWeather.mapToDtoWeather(weatherResponse);
    }

    public List<DtoWeather> getForecasts() {
        List<EntityWeather> entities = weatherRepository.getAll();
        return entities.stream().map(mapperWeather::mapToDtoWeather).toList();
    }

    public DtoWeather getForecast(Long id) {
        EntityWeather entity = weatherRepository.getById(id);
        return mapperWeather.mapToDtoWeather(entity);
    }

    public void deleteForecast(Long id) {
        weatherRepository.deleteById(id);
    }
}
