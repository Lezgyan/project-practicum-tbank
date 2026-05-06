package ru.tbank.practicum.service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.entity.statePayload.BlindsStatePayload;
import ru.tbank.practicum.enums.AutoDeviceCommand;
import ru.tbank.practicum.enums.BlindsState;
import ru.tbank.practicum.enums.DeviceType;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlindsService implements DeviceService {

    private final DeviceCommandService deviceCommandService;

    @Override
    public void apply(Device device, ZonedDateTime now) {
        if (device.getType() != DeviceType.BLINDS) {
            return;
        }

        applyTimeRules(device, now);
        applyWeatherRule(device);
    }

    private void applyTimeRules(Device device, ZonedDateTime now) {

        DeviceSettings settings = device.getSettings();

        if (settings == null) {
            return;
        }

        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(device.getRoom().getTimezone());
        } catch (Exception e) {
            log.warn(
                    "Пропущена обработка штор: некорректный timezone='{}', deviceId={}",
                    device.getRoom().getTimezone(),
                    device.getId());
            return;
        }

        LocalTime openTime = settings.getBlindsOpenTime();

        LocalTime closeTime = settings.getBlindsCloseTime();

        if (openTime == null && closeTime == null) {
            log.info("openTime и closeTime равны null: {}", device.getId());
            return;
        }

        LocalTime localNow = now.withZoneSameInstant(zoneId).toLocalTime();

        Boolean desiredOpen = calculateDesiredOpenState(localNow, openTime, closeTime);
        if (desiredOpen == null) {
            log.info("desiredOpen равен null: {}", device.getId());
            return;
        }

        if (isBlindsAlready(device, desiredOpen)) {
            log.info("isBlindsAlready равен false: {}", device.getId());
            return;
        }

        if (desiredOpen) {
            deviceCommandService.setBlinds(device, BlindsState.OPEN, AutoDeviceCommand.AUTO_TIME_OPEN);
            log.info("Auto open blinds by time rule: {}", device.getId());
        } else {
            deviceCommandService.setBlinds(device, BlindsState.CLOSED, AutoDeviceCommand.AUTO_TIME_CLOSE);
            log.info("Auto close blinds by time rule: {}", device.getId());
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
            deviceCommandService.setBlinds(device, BlindsState.CLOSED, AutoDeviceCommand.AUTO_BRIGHT_SUN);

            log.info("Auto close blinds because of weather: {}", device.getId());
        }
    }

    private Boolean calculateDesiredOpenState(LocalTime now, LocalTime openTime, LocalTime closeTime) {
        if (openTime == null && closeTime == null) {
            return null;
        }

        if (openTime != null && closeTime == null) {
            return !now.isBefore(openTime);
        }

        if (openTime == null) {
            return now.isBefore(closeTime);
        }

        if (openTime.equals(closeTime)) {
            return null;
        }

        if (openTime.isBefore(closeTime)) {
            return !now.isBefore(openTime) && now.isBefore(closeTime);
        }

        return !now.isBefore(openTime) || now.isBefore(closeTime);
    }

    private boolean isBlindsAlready(Device device, boolean desiredOpen) {
        DeviceState deviceState = device.getDeviceState();

        if (deviceState == null) {
            return false;
        }

        boolean state = ((BlindsStatePayload) deviceState.getDeviceStatePayload()).isOpen();

        return state == desiredOpen;
    }
}
