package ru.tbank.practicum.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.tbank.practicum.dto.external.CloudsInfo;
import ru.tbank.practicum.dto.external.DtoWeatherResponse;
import ru.tbank.practicum.dto.external.MainWeatherInfo;
import ru.tbank.practicum.dto.external.RainInfo;
import ru.tbank.practicum.dto.external.SnowInfo;
import ru.tbank.practicum.dto.external.WeatherCondition;
import ru.tbank.practicum.dto.external.WindInfo;
import ru.tbank.practicum.entity.WeatherMeasurement;

class MapperWeatherTest {

    private final MapperWeather mapperWeather = Mappers.getMapper(MapperWeather.class);

    @Test
    void mapToEntityWeather_validDto_mapsAllFieldsToNewEntity() {
        DtoWeatherResponse dtoWeatherResponse = weatherResponse(
                1.0,
                2.0,
                3,
                4,
                5.0,
                6,
                7.0,
                null,
                88005553535L,
                List.of(new WeatherCondition(500, "Rain", "light rain", "10d")));

        WeatherMeasurement result = mapperWeather.mapToEntityWeather(dtoWeatherResponse);

        assertThat(result.getId()).isNull();
        assertThat(result.getTemperature()).isEqualTo(1.);
        assertThat(result.getFeelsLike()).isEqualTo(2.0);
        assertThat(result.getHumidity()).isEqualTo(3);
        assertThat(result.getPressure()).isEqualTo(4);
        assertThat(result.getWindSpeed()).isEqualTo(5.0);
        assertThat(result.getCloudiness()).isEqualTo(6);
        assertThat(result.getIsRaining()).isTrue();
        assertThat(result.getIsSnowing()).isFalse();
        assertThat(result.getWeatherMain()).isEqualTo("Rain");
        assertThat(result.getWeatherDescription()).isEqualTo("light rain");
        assertThat(result.getMeasuredAt())
                .isEqualTo(OffsetDateTime.ofInstant(Instant.ofEpochSecond(88005553535L), ZoneOffset.UTC));
    }

    @Test
    void updateEntityWeather_validDto_updatesExistingEntityAndKeepsId() {
        WeatherMeasurement entityWeather = new WeatherMeasurement();
        entityWeather.setId(100L);

        DtoWeatherResponse dtoWeatherResponse = weatherResponse(
                10.0,
                11.0,
                12,
                13,
                14.0,
                15,
                null,
                0.7,
                111L,
                List.of(new WeatherCondition(800, "Clear", "clear sky", "01d")));

        WeatherMeasurement result = mapperWeather.updateEntityWeather(entityWeather, dtoWeatherResponse);

        assertThat(result).isSameAs(entityWeather);
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getTemperature()).isEqualTo(10.0);
        assertThat(result.getFeelsLike()).isEqualTo(11.0);
        assertThat(result.getHumidity()).isEqualTo(12);
        assertThat(result.getPressure()).isEqualTo(13);
        assertThat(result.getWindSpeed()).isEqualTo(14.0);
        assertThat(result.getCloudiness()).isEqualTo(15);
        assertThat(result.getIsRaining()).isFalse();
        assertThat(result.getIsSnowing()).isTrue();
        assertThat(result.getWeatherMain()).isEqualTo("Clear");
        assertThat(result.getWeatherDescription()).isEqualTo("clear sky");
        assertThat(result.getMeasuredAt())
                .isEqualTo(OffsetDateTime.ofInstant(Instant.ofEpochSecond(111L), ZoneOffset.UTC));
    }

    @Test
    void isRainingOrSnowing_precipitationIsGreaterThanZero_returnsTrue() {
        Boolean result = mapperWeather.isRainingOrSnowing(0.5);

        assertThat(result).isTrue();
    }

    @Test
    void isRainingOrSnowing_precipitationIsZero_returnsFalse() {
        Boolean result = mapperWeather.isRainingOrSnowing(0.0);

        assertThat(result).isFalse();
    }

    @Test
    void isRainingOrSnowing_precipitationIsNull_returnsFalse() {
        Boolean result = mapperWeather.isRainingOrSnowing(null);

        assertThat(result).isFalse();
    }

    @Test
    void toOffsetDateTime_validTimestamp_returnsUtcOffsetDateTime() {
        OffsetDateTime result = mapperWeather.toOffsetDateTime(1_700_000_000L);

        assertThat(result).isEqualTo(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1_700_000_000L), ZoneOffset.UTC));
    }

    @Test
    void firstConditionGroup_weatherConditionListHasFirstElement_returnsFirstConditionGroup() {
        String result = mapperWeather.firstConditionGroup(
                List.of(new WeatherCondition(801, "Clouds", "overcast clouds", "04d")));

        assertThat(result).isEqualTo("Clouds");
    }

    @Test
    void firstConditionGroup_weatherConditionListIsNull_returnsNull() {
        String result = mapperWeather.firstConditionGroup(null);

        assertThat(result).isNull();
    }

    @Test
    void firstConditionGroup_weatherConditionListIsEmpty_returnsNull() {
        String result = mapperWeather.firstConditionGroup(List.of());

        assertThat(result).isNull();
    }

    @Test
    void firstDescription_weatherConditionListHasFirstElement_returnsFirstDescription() {
        String result =
                mapperWeather.firstDescription(List.of(new WeatherCondition(803, "Clouds", "broken clouds", "04d")));

        assertThat(result).isEqualTo("broken clouds");
    }

    @Test
    void firstDescription_weatherConditionListIsNull_returnsNull() {
        String result = mapperWeather.firstDescription(null);

        assertThat(result).isNull();
    }

    @Test
    void firstDescription_weatherConditionListIsEmpty_returnsNull() {
        String result = mapperWeather.firstDescription(List.of());

        assertThat(result).isNull();
    }

    private DtoWeatherResponse weatherResponse(
            Double temperature,
            Double feelsLike,
            Integer humidity,
            Integer pressure,
            Double windSpeed,
            Integer cloudiness,
            Double rainOneHourPrecipitation,
            Double snowOneHourPrecipitation,
            Long timestamp,
            List<WeatherCondition> weatherCondition) {
        return DtoWeatherResponse.builder()
                .main(MainWeatherInfo.builder()
                        .temperature(temperature)
                        .feelsLike(feelsLike)
                        .humidity(humidity)
                        .pressure(pressure)
                        .build())
                .weatherCondition(weatherCondition)
                .windInfo(new WindInfo(windSpeed, null, null))
                .cloudsInfo(new CloudsInfo(cloudiness))
                .rainInfo(rainOneHourPrecipitation == null ? null : new RainInfo(rainOneHourPrecipitation))
                .snowInfo(snowOneHourPrecipitation == null ? null : new SnowInfo(snowOneHourPrecipitation))
                .timestamp(timestamp)
                .build();
    }
}
