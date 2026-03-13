package ru.tbank.practicum.dto.external;

import java.util.List;

public record DtoWeatherResponse(
         Coord coord,
         List<Weather> weather,
         String base,
         CommonData main,
         int visibility,
         Wind wind,
         Rain rain,
         Clouds clouds,
         long dt,
         LocationData sys,
         int timezone,
         long id,
         String name,
         int cod
) {
}