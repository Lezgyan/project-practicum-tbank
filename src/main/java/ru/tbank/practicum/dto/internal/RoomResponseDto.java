package ru.tbank.practicum.dto.internal;

import java.util.List;

public record RoomResponseDto(
        Long id,
        String name,
        Double lon,
        Double lat,
        String timezone,
        WeatherResponseDto weather,
        List<DeviceShortDto> devices) {}
