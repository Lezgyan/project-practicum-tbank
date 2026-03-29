package ru.tbank.practicum.service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.entity.statePayload.BlindsStatePayload;
import ru.tbank.practicum.enums.DeviceType;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlindsService implements DeviceService {

    @Override
    public void apply(Device device) {
        if (device.getType() != DeviceType.BLINDS) {
            return;
        }
        applyTimeRules(device);
        applyWeatherRule(device);
    }

    private void applyTimeRules(Device device) {
        LocalTime now = LocalTime.now(ZoneId.of(device.getRoom().getTimezone())).truncatedTo(ChronoUnit.MINUTES);

        DeviceSettings settings = device.getSettings();

        if (settings == null) {
            return;
        }

        if (settings.getBlindsOpenTime() != null && now.equals(settings.getBlindsOpenTime())) {
            if (!isBlindsAlready(device, true)) {
                // SENT KAFKA

                // deviceCommandService.setBlinds(device.getId(), true, "AUTO_TIME_OPEN");
                log.info("Auto open blinds: {}", device.getExternalId());
            }
        }

        if (settings.getBlindsCloseTime() != null && now.equals(settings.getBlindsCloseTime())) {
            if (!isBlindsAlready(device, false)) {
                // SENT KAFKA

                // deviceCommandService.setBlinds(device.getId(), false, "AUTO_TIME_CLOSE");
                log.info("Auto close blinds: {}", device.getExternalId());
            }
        }
    }

    private void applyWeatherRule(Device device) {
        WeatherMeasurement weather = device.getRoom().getWeather();
        DeviceSettings deviceSettings = device.getSettings();

        boolean brightSun = weather.getCloudiness() != null
                && deviceSettings.getMinCloudinessWhenNormal() != null
                && weather.getCloudiness() < deviceSettings.getMinCloudinessWhenNormal()
                && !weather.getIsRaining();

        boolean hot = weather.getTemperature() != null
                && deviceSettings.getMinTemperatureOutsideWhenNeedCloseBlinds() != null
                && weather.getTemperature().compareTo(deviceSettings.getMinTemperatureOutsideWhenNeedCloseBlinds())
                        >= 0;

        if ((brightSun || hot) && !isBlindsAlready(device, false)) {
            // SENT KAFKA

            //            deviceCommandService.setBlinds(device.getId(), false, "AUTO_BRIGHT_SUN");
            log.info("Auto close blinds because of weather: {}", device.getExternalId());
        }
    }

    private boolean isBlindsAlready(Device device, boolean desiredOpen) {
        DeviceState deviceState = device.getDeviceState();

        if (deviceState == null) {
            return false;
        }

        Boolean state = ((BlindsStatePayload) deviceState.getDeviceStatePayload()).getOpen();

        return state != null && state.equals(desiredOpen);
    }
}
