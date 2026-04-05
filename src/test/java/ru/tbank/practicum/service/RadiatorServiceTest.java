package ru.tbank.practicum.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.entity.statePayload.RadiatorStatePayload;
import ru.tbank.practicum.enums.DeviceType;

class RadiatorServiceTest {

    private final RadiatorService radiatorService = new RadiatorService();

    @Test
    void apply_deviceTypeIsNotRadiator_doesNothing() {
        Device device = new Device();
        device.setType(DeviceType.BLINDS);

        assertThatCode(() -> radiatorService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    void apply_radiatorSettingsAreNull_doesNothing() {
        Device device = new Device();
        device.setType(DeviceType.RADIATOR);
        device.setRoom(new Room());

        assertThatCode(() -> radiatorService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    void apply_outsideTemperatureIsNull_doesNothing() {
        Device device = new Device();
        device.setType(DeviceType.RADIATOR);

        DeviceSettings settings = new DeviceSettings();
        settings.setColdWeatherTemperature(0.0);
        settings.setRadiatorTempWhenCold(25.0);
        device.setSettings(settings);

        WeatherMeasurement weather = new WeatherMeasurement();
        weather.setTemperature(null);

        Room room = new Room();
        room.setWeather(weather);
        device.setRoom(room);

        assertThatCode(() -> radiatorService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    void apply_coldWeatherRuleMatchesAndStateIsMissing_completesWithoutException() {
        Device device = new Device();
        device.setType(DeviceType.RADIATOR);
        device.setExternalId(java.util.UUID.randomUUID());

        DeviceSettings settings = new DeviceSettings();
        settings.setColdWeatherTemperature(0.0);
        settings.setRadiatorTempWhenCold(25.0);
        device.setSettings(settings);

        WeatherMeasurement weather = new WeatherMeasurement();
        weather.setTemperature(-5.0);

        Room room = new Room();
        room.setWeather(weather);
        device.setRoom(room);

        assertThatCode(() -> radiatorService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    void apply_hotWeatherRuleMatchesAndRadiatorAlreadyHasDesiredTemperature_doesNothing() {
        Device device = new Device();
        device.setType(DeviceType.RADIATOR);
        device.setExternalId(java.util.UUID.randomUUID());

        DeviceSettings settings = new DeviceSettings();
        settings.setHotWeatherTemperature(25.0);
        settings.setRadiatorTempWhenHot(10.0);
        device.setSettings(settings);

        WeatherMeasurement weather = new WeatherMeasurement();
        weather.setTemperature(30.0);

        Room room = new Room();
        room.setWeather(weather);
        device.setRoom(room);

        DeviceState deviceState = new DeviceState();
        RadiatorStatePayload payload = new RadiatorStatePayload();
        payload.setTemperature(10.0);
        deviceState.setDeviceStatePayload(payload);
        device.setDeviceState(deviceState);

        assertThatCode(() -> radiatorService.apply(device)).doesNotThrowAnyException();
    }
}
