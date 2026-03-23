package ru.tbank.practicum.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EntityWeather {
    private Long id;
    private Double temperature;
    private String description;
    private Integer pressure;
    private Double windSpeed;
    private Double latCoordinate;
    private Double lonCoordinate;
}
