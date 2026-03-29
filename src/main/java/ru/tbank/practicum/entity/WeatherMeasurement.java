package ru.tbank.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "weather_measurement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "measured_at", nullable = false)
    private OffsetDateTime measuredAt;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "feels_like")
    private Double feelsLike;

    @Column(name = "humidity")
    private Integer humidity;

    @Column(name = "pressure")
    private Integer pressure;

    @Column(name = "weather_main")
    private String weatherMain;

    @Column(name = "weather_description")
    private String weatherDescription;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(name = "cloudiness")
    private Integer cloudiness;

    @Column(name = "is_raining", nullable = false)
    private Boolean isRaining;

    @Column(name = "is_snowing", nullable = false)
    private Boolean isSnowing;
}
