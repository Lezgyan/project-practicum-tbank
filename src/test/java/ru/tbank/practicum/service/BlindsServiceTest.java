package ru.tbank.practicum.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.Room;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.entity.statePayload.BlindsStatePayload;
import ru.tbank.practicum.enums.DeviceType;

@ExtendWith(MockitoExtension.class)
class BlindsServiceTest {

    @Mock
    private DeviceCommandService deviceCommandService;

    @InjectMocks
    private BlindsService blindsService;

    @BeforeEach
    public void setUp() {
        blindsService.beforeBatch();
    }

    @AfterEach
    public void down() {
        blindsService.afterBatch();
    }

    @Test
    public void apply_deviceTypeIsNotBlinds_doesNothing() {
        Device device = new Device();
        device.setType(DeviceType.RADIATOR);

        assertThatCode(() -> blindsService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    public void apply_blindsSettingsAreNull_doesNothing() {
        Device device = new Device();
        device.setType(DeviceType.BLINDS);

        Room room = new Room();
        room.setTimezone("Europe/Saratov");
        room.setWeather(new WeatherMeasurement());

        DeviceSettings deviceSettings = new DeviceSettings();
        device.setRoom(room);
        device.setSettings(deviceSettings);

        assertThatCode(() -> blindsService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    public void apply_openTimeMatchesAndBlindsAreClosed_completesWithoutException() {
        Device device = new Device();
        device.setType(DeviceType.BLINDS);
        device.setExternalId(java.util.UUID.randomUUID());

        DeviceSettings settings = new DeviceSettings();
        settings.setBlindsOpenTime(LocalTime.now(java.time.ZoneId.of("Europe/Saratov"))
                .withSecond(0)
                .withNano(0));
        device.setSettings(settings);

        WeatherMeasurement weather = new WeatherMeasurement();
        weather.setCloudiness(100);
        weather.setIsRaining(false);
        weather.setTemperature(10.0);

        Room room = new Room();
        room.setTimezone("Europe/Saratov");
        room.setWeather(weather);
        device.setRoom(room);

        DeviceState deviceState = new DeviceState();
        BlindsStatePayload payload = new BlindsStatePayload();
        payload.setOpen(false);
        deviceState.setDeviceStatePayload(payload);
        device.setDeviceState(deviceState);

        assertThatCode(() -> blindsService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    public void apply_brightSunConditionMatches_completesWithoutException() {
        Device device = new Device();
        device.setType(DeviceType.BLINDS);
        device.setExternalId(java.util.UUID.randomUUID());

        DeviceSettings settings = new DeviceSettings();
        settings.setMinCloudinessWhenNormal(50.0);
        settings.setMinTemperatureOutsideWhenNeedCloseBlinds(30.0);
        device.setSettings(settings);

        WeatherMeasurement weather = new WeatherMeasurement();
        weather.setCloudiness(10);
        weather.setIsRaining(false);
        weather.setTemperature(20.0);

        Room room = new Room();
        room.setTimezone("Europe/Saratov");
        room.setWeather(weather);
        device.setRoom(room);

        DeviceState deviceState = new DeviceState();
        BlindsStatePayload payload = new BlindsStatePayload();
        payload.setOpen(true);
        deviceState.setDeviceStatePayload(payload);
        device.setDeviceState(deviceState);

        assertThatCode(() -> blindsService.apply(device)).doesNotThrowAnyException();
    }

    @Test
    public void apply_hotWeatherConditionMatches_completesWithoutException() {
        Device device = new Device();
        device.setType(DeviceType.BLINDS);
        device.setExternalId(java.util.UUID.randomUUID());

        DeviceSettings settings = new DeviceSettings();
        settings.setMinCloudinessWhenNormal(5.0);
        settings.setMinTemperatureOutsideWhenNeedCloseBlinds(25.0);
        device.setSettings(settings);

        WeatherMeasurement weather = new WeatherMeasurement();
        weather.setCloudiness(90);
        weather.setIsRaining(false);
        weather.setTemperature(30.0);

        Room room = new Room();
        room.setTimezone("Europe/Moscow");
        room.setWeather(weather);
        device.setRoom(room);

        DeviceState deviceState = new DeviceState();
        BlindsStatePayload payload = new BlindsStatePayload();
        payload.setOpen(true);
        deviceState.setDeviceStatePayload(payload);
        device.setDeviceState(deviceState);

        assertThatCode(() -> blindsService.apply(device)).doesNotThrowAnyException();
    }
}
