package ru.tbank.practicum.mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.external.WeatherCondition;
import ru.tbank.practicum.entity.WeatherMeasurement;

@Mapper(componentModel = "spring")
public interface MapperWeather {

    default WeatherMeasurement mapToEntityWeather(DtoWeatherResponse dtoWeatherResponse) {
        return updateEntityWeather(new WeatherMeasurement(), dtoWeatherResponse);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "main.temperature", target = "temperature")
    @Mapping(source = "main.feelsLike", target = "feelsLike")
    @Mapping(source = "main.humidity", target = "humidity")
    @Mapping(source = "weatherCondition", target = "weatherDescription", qualifiedByName = "firstDescription")
    @Mapping(source = "weatherCondition", target = "weatherMain", qualifiedByName = "firstConditionGroup")
    @Mapping(source = "main.pressure", target = "pressure")
    @Mapping(source = "windInfo.speed", target = "windSpeed")
    @Mapping(source = "cloudsInfo.cloudiness", target = "cloudiness")
    @Mapping(source = "timestamp", target = "measuredAt", qualifiedByName = "toOffsetDateTime")
    @Mapping(source = "rainInfo.oneHourPrecipitation", target = "isRaining", qualifiedByName = "isPrecipitation")
    @Mapping(source = "snowInfo.oneHourPrecipitation", target = "isSnowing", qualifiedByName = "isPrecipitation")
    WeatherMeasurement updateEntityWeather(
            @MappingTarget WeatherMeasurement entityWeather, DtoWeatherResponse dtoWeatherResponse);

    @Named("isPrecipitation")
    default Boolean isRainingOrSnowing(Double oneHourPrecipitation) {
        if (oneHourPrecipitation != null) {
            return oneHourPrecipitation > 0.0;
        }
        return false;
    }

    @Named("toOffsetDateTime")
    default OffsetDateTime toOffsetDateTime(Long timestamp) {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
    }

    @Named("firstConditionGroup")
    default String firstConditionGroup(List<WeatherCondition> weatherCondition) {
        if (weatherCondition != null && !weatherCondition.isEmpty() && weatherCondition.getFirst() != null) {
            return weatherCondition.getFirst().conditionGroup();
        }
        return null;
    }

    @Named("firstDescription")
    default String firstDescription(List<WeatherCondition> weatherCondition) {
        if (weatherCondition != null && !weatherCondition.isEmpty() && weatherCondition.getFirst() != null) {
            return weatherCondition.getFirst().description();
        }
        return null;
    }
}
