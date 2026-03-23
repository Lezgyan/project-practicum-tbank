package ru.tbank.practicum.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.external.WeatherCondition;
import ru.tbank.practicum.dto.internal.DtoWeather;
import ru.tbank.practicum.entity.EntityWeather;

@Mapper(componentModel = "spring")
public interface MapperWeather {
    @Mapping(source = "main.temperature", target = "temperature")
    @Mapping(source = "cityId", target = "id")
    @Mapping(source = "weatherCondition", target = "description", qualifiedByName = "firstDescription")
    DtoWeather mapToDtoWeather(DtoWeatherResponse dtoWeatherResponse);

    DtoWeather mapToDtoWeather(EntityWeather entityWeather);

    @Mapping(source = "main.temperature", target = "temperature")
    @Mapping(source = "weatherCondition", target = "description", qualifiedByName = "firstDescription")
    @Mapping(source = "main.pressure", target = "pressure")
    @Mapping(source = "windInfo.speed", target = "windSpeed")
    @Mapping(source = "coordinates.lat", target = "latCoordinate")
    @Mapping(source = "coordinates.lon", target = "lonCoordinate")
    @Mapping(source = "cityId", target = "id")
    EntityWeather mapToEntityWeather(DtoWeatherResponse dtoWeatherResponse);

    @Named("firstDescription")
    default String firstDescription(List<WeatherCondition> weatherCondition) {
        if (weatherCondition != null && !weatherCondition.isEmpty() && weatherCondition.getFirst() != null) {
            return weatherCondition.getFirst().description();
        }
        return null;
    }
}
