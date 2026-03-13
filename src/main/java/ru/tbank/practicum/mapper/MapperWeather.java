package ru.tbank.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.external.Weather;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.entity.EntityWeather;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperWeather {
    @Mapping(source = "main.temp", target = "temperature")
    @Mapping(source = "weather", target = "description", qualifiedByName = "firstDescription")
    DtoWeather mapToDtoWeather(DtoWeatherResponse dtoWeatherResponse);
    DtoWeather mapToDtoWeather(EntityWeather entityWeather);

    @Named("firstDescription")
    default String firstDescription(List<Weather> weather) {
        return weather.getFirst().description();
    }
}
