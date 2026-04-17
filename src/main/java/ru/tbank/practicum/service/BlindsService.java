package ru.tbank.practicum.service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
    private ZonedDateTime batchPreviousRunAt;
    private ZonedDateTime batchCurrentRunAt;
    private ZonedDateTime lastRunAt;

    @Override
    public void beforeBatch() {
        batchCurrentRunAt = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        batchPreviousRunAt = lastRunAt == null ? batchCurrentRunAt.minusMinutes(1) : lastRunAt;
    }

    @Override
    public void apply(Device device) {
        if (device.getType() != DeviceType.BLINDS) {
            return;
        }
        applyTimeRules(device);
        applyWeatherRule(device);
    }

    @Override
    public void afterBatch() {
        lastRunAt = batchCurrentRunAt;
    }

    private void applyTimeRules(Device device) {

        DeviceSettings settings = device.getSettings();

        if (settings == null) {
            return;
        }

        ZoneId zoneId = ZoneId.of(device.getRoom().getTimezone());

        LocalTime from = batchPreviousRunAt
                .withZoneSameInstant(zoneId)
                .truncatedTo(ChronoUnit.MINUTES)
                .toLocalTime();

        LocalTime to = batchCurrentRunAt
                .withZoneSameInstant(zoneId)
                .truncatedTo(ChronoUnit.MINUTES)
                .toLocalTime();

        LocalTime openTime = settings.getBlindsOpenTime();

        if (openTime != null && isTimeInWindow(openTime, from, to) && !isBlindsAlready(device, true)) {

            // SENT KAFKA
            deviceCommandService.setBlinds(device, BlindsState.OPEN, AutoDeviceCommand.AUTO_TIME_OPEN);

            log.info("Auto open blinds: {}", device.getId());
        }
        LocalTime closeTime = settings.getBlindsCloseTime();

        if (closeTime != null && isTimeInWindow(closeTime, from, to) && !isBlindsAlready(device, false)) {
            // SENT KAFKA
            deviceCommandService.setBlinds(device, BlindsState.CLOSED, AutoDeviceCommand.AUTO_TIME_CLOSE);

            log.info("Auto close blinds: {}", device.getId());
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

    private boolean isTimeInWindow(LocalTime target, LocalTime from, LocalTime to) {
        if (from.equals(to)) {
            return false;
        }

        if (from.isBefore(to)) {
            return target.isAfter(from) && (target.equals(to) || target.isBefore(to));
        }

        return target.isAfter(from) || target.equals(to) || target.isBefore(to);
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
