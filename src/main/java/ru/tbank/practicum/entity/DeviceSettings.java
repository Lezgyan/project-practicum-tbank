package ru.tbank.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "device_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "blinds_open_time")
    private LocalTime blindsOpenTime;

    @Column(name = "blinds_close_time")
    private LocalTime blindsCloseTime;

    @Column(name = "cold_weather_temperature")
    private Double coldWeatherTemperature;

    @Column(name = "hot_weather_temperature")
    private Double hotWeatherTemperature;

    @Column(name = "radiator_temp_when_cold")
    private Double radiatorTempWhenCold;

    @Column(name = "radiator_temp_when_hot")
    private Double radiatorTempWhenHot;

    @Column(name = "min_cloudiness_when_normal")
    private Double minCloudinessWhenNormal;

    @Column(name = "min_temperature_outside_when_need_close_blinds")
    private Double minTemperatureOutsideWhenNeedCloseBlinds;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;
}
