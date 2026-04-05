package ru.tbank.practicum.dto.internal;

import java.time.LocalTime;

public record UpdateDeviceSettingsResponse(
        LocalTime blindsOpenTime,
        LocalTime blindsCloseTime,
        Double coldWeatherTemperature,
        Double hotWeatherTemperature,
        Double radiatorTempWhenCold,
        Double radiatorTempWhenHot,
        Double minCloudinessWhenNormal,
        Double minTemperatureOutsideWhenNeedCloseBlinds) {}
