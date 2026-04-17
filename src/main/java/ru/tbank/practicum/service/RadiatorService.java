package ru.tbank.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.entity.Device;
import ru.tbank.practicum.entity.DeviceSettings;
import ru.tbank.practicum.entity.DeviceState;
import ru.tbank.practicum.entity.WeatherMeasurement;
import ru.tbank.practicum.entity.statePayload.RadiatorStatePayload;
import ru.tbank.practicum.enums.AutoDeviceCommand;
import ru.tbank.practicum.enums.DeviceType;

@Slf4j
@Service
@RequiredArgsConstructor
public class RadiatorService implements DeviceService {

    private final DeviceCommandService deviceCommandService;

    @Override
    public void apply(Device device) {
        if (device.getType() != DeviceType.RADIATOR) {
            return;
        }
        applyWeatherRules(device);
    }

    private void applyWeatherRules(Device device) {

        DeviceSettings settings = device.getSettings();

        if (settings == null) {
            return;
        }

        WeatherMeasurement weatherMeasurement = device.getRoom().getWeather();

        applyRadiatorRule(device, settings, weatherMeasurement);
    }

    private void applyRadiatorRule(Device device, DeviceSettings settings, WeatherMeasurement weather) {
        Double outsideTemp = weather.getTemperature();
        if (outsideTemp == null) {
            return;
        }

        if (settings.getColdWeatherTemperature() != null
                && settings.getRadiatorTempWhenCold() != null
                && outsideTemp.compareTo(settings.getColdWeatherTemperature()) <= 0) {

            if (!isRadiatorAlready(device, settings.getRadiatorTempWhenCold())) {
                // SENT KAFKA
                deviceCommandService.setRadiatorTemperature(
                        device, settings.getRadiatorTempWhenCold(), AutoDeviceCommand.AUTO_COLD_WEATHER);
                log.info("Auto radiator cold rule: {}", device.getExternalId());
            }

        } else if (settings.getHotWeatherTemperature() != null
                && settings.getRadiatorTempWhenHot() != null
                && outsideTemp.compareTo(settings.getHotWeatherTemperature()) >= 0) {

            if (!isRadiatorAlready(device, settings.getRadiatorTempWhenHot())) {
                // SENT KAFKA
                deviceCommandService.setRadiatorTemperature(
                        device, settings.getRadiatorTempWhenHot(), AutoDeviceCommand.AUTO_HOT_WEATHER);
                log.info("Auto radiator hot rule: {}", device.getExternalId());
            }
        }
    }

    private boolean isRadiatorAlready(Device device, Double desiredTemp) {
        DeviceState deviceState = device.getDeviceState();

        if (deviceState == null) {
            return false;
        }

        double state = ((RadiatorStatePayload) deviceState.getDeviceStatePayload()).getTemperature();

        return Double.compare(state, desiredTemp) == 0;
    }
}
