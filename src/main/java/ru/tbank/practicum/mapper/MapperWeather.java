package ru.tbank.practicum.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.external.Weather;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.entity.EntityWeather;

@Mapper(componentModel = "spring")
public interface MapperWeather {
    @Mapping(source = "main.temp", target = "temperature")
    @Mapping(source = "weather", target = "description", qualifiedByName = "firstDescription")
    DtoWeather mapToDtoWeather(DtoWeatherResponse dtoWeatherResponse);

    DtoWeather mapToDtoWeather(EntityWeather entityWeather);

    @Mapping(source = "main.temp", target = "temperature")
    @Mapping(source = "weather", target = "description", qualifiedByName = "firstDescription")
    @Mapping(source = "main.pressure", target = "pressure")
    @Mapping(source = "wind.speed", target = "windSpeed")
    @Mapping(source = "coord.lat", target = "latCoordinate")
    @Mapping(source = "coord.lon", target = "lonCoordinate")
    EntityWeather mapToEntityWeather(DtoWeatherResponse dtoWeatherResponse);

    @Named("firstDescription")
    default String firstDescription(List<Weather> weather) {
        return weather.getFirst().description();
    }
}
