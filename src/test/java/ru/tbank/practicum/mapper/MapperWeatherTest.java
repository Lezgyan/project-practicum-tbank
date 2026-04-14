package ru.tbank.practicum.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void mapToEntityWeather_validDto_mapsAllFieldsToNewEntity() {
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

        assertNull(result.getId());

        assertEquals(1., result.getTemperature());
        assertEquals(2.0, result.getFeelsLike());
        assertEquals(3, result.getHumidity());
        assertEquals(4, result.getPressure());
        assertEquals(5.0, result.getWindSpeed());
        assertEquals(6, result.getCloudiness());

        assertTrue(result.getIsRaining());
        assertFalse(result.getIsSnowing());

        assertEquals("Rain", result.getWeatherMain());

        assertEquals("light rain", result.getWeatherDescription());

        assertEquals(
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(88005553535L), ZoneOffset.UTC), result.getMeasuredAt());
    }

    @Test
    public void updateEntityWeather_validDto_updatesExistingEntityAndKeepsId() {
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

        assertEquals(100L, result.getId());
        assertEquals(10.0, result.getTemperature());
        assertEquals(11.0, result.getFeelsLike());
        assertEquals(12, result.getHumidity());
        assertEquals(13, result.getPressure());
        assertEquals(14.0, result.getWindSpeed());
        assertEquals(15, result.getCloudiness());

        assertFalse(result.getIsRaining());
        assertTrue(result.getIsSnowing());
        assertEquals("Clear", result.getWeatherMain());
        assertEquals("clear sky", result.getWeatherDescription());
        assertEquals(OffsetDateTime.ofInstant(Instant.ofEpochSecond(111L), ZoneOffset.UTC), result.getMeasuredAt());
    }

    @Test
    public void isRainingOrSnowing_precipitationIsGreaterThanZero_returnsTrue() {
        Boolean result = mapperWeather.isRainingOrSnowing(0.5);

        assertTrue(result);
    }

    @Test
    public void isRainingOrSnowing_precipitationIsZero_returnsFalse() {
        Boolean result = mapperWeather.isRainingOrSnowing(0.0);

        assertFalse(result);
    }

    @Test
    public void isRainingOrSnowing_precipitationIsNull_returnsFalse() {
        Boolean result = mapperWeather.isRainingOrSnowing(null);

        assertFalse(result);
    }

    @Test
    public void toOffsetDateTime_validTimestamp_returnsUtcOffsetDateTime() {
        OffsetDateTime result = mapperWeather.toOffsetDateTime(1_700_000_000L);

        assertEquals(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1_700_000_000L), ZoneOffset.UTC), result);
    }

    @Test
    public void firstConditionGroup_weatherConditionListHasFirstElement_returnsFirstConditionGroup() {
        String result = mapperWeather.firstConditionGroup(
                List.of(new WeatherCondition(801, "Clouds", "overcast clouds", "04d")));

        assertEquals("Clouds", result);
    }

    @Test
    public void firstConditionGroup_weatherConditionListIsNull_returnsNull() {
        String result = mapperWeather.firstConditionGroup(null);

        assertNull(result);
    }

    @Test
    public void firstConditionGroup_weatherConditionListIsEmpty_returnsNull() {
        String result = mapperWeather.firstConditionGroup(List.of());

        assertNull(result);
    }

    @Test
    public void firstDescription_weatherConditionListHasFirstElement_returnsFirstDescription() {
        String result =
                mapperWeather.firstDescription(List.of(new WeatherCondition(803, "Clouds", "broken clouds", "04d")));

        assertEquals("broken clouds", result);
    }

    @Test
    public void firstDescription_weatherConditionListIsNull_returnsNull() {
        String result = mapperWeather.firstDescription(null);

        assertNull(result);
    }

    @Test
    public void firstDescription_weatherConditionListIsEmpty_returnsNull() {
        String result = mapperWeather.firstDescription(List.of());

        assertNull(result);
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
