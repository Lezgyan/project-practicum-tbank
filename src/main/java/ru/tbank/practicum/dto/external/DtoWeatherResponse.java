package ru.tbank.practicum.dto.external;

import java.util.List;

public record DtoWeatherResponse(
        Coord coord,
        List<Weather> weather,
        String base,
        CommonData main,
        Integer visibility,
        Wind wind,
        Rain rain,
        Clouds clouds,
        Long dt,
        LocationData sys,
        Integer timezone,
        Long id,
        String name,
        Integer cod) {}
