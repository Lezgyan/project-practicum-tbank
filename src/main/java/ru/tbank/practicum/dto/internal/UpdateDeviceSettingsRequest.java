package ru.tbank.practicum.dto.internal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import ru.tbank.practicum.validation.LessThan;

@LessThan(less = "blindsOpenTime", more = "blindsCloseTime")
@LessThan(less = "coldWeatherTemperature", more = "hotWeatherTemperature")
@LessThan(less = "radiatorTempWhenCold", more = "radiatorTempWhenHot")
public record UpdateDeviceSettingsRequest(
        @Schema(type = "string", example = "10:00") @JsonFormat(pattern = "HH:mm")
        LocalTime blindsOpenTime,

        @Schema(type = "string", example = "12:00") @JsonFormat(pattern = "HH:mm")
        LocalTime blindsCloseTime,

        Double coldWeatherTemperature,
        Double hotWeatherTemperature,
        Double radiatorTempWhenCold,
        Double radiatorTempWhenHot,
        Double minCloudinessWhenNormal,
        Double minTemperatureOutsideWhenNeedCloseBlinds) {}
