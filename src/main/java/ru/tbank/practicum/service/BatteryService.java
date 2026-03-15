package ru.tbank.practicum.service;

import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.internal.DtoTemperature;
import ru.tbank.practicum.entity.EntityTemperature;
import ru.tbank.practicum.mapper.MapperTemperature;
import ru.tbank.practicum.repository.BatteryRepository;

@Service
public class BatteryService {

    private final BatteryRepository batteryRepository;
    private final MapperTemperature mapperTemperature;

    public BatteryService(BatteryRepository batteryRepository, MapperTemperature mapperTemperature) {
        this.batteryRepository = batteryRepository;
        this.mapperTemperature = mapperTemperature;
    }

    public void upTemperature(DtoTemperature degree) {
        EntityTemperature entityTemperature = mapperTemperature.mapToEntityTemperature(degree);
        batteryRepository.upTemperature(entityTemperature);
    }

    public void downTemperature(DtoTemperature degree) {
        EntityTemperature entityTemperature = mapperTemperature.mapToEntityTemperature(degree);
        batteryRepository.downTemperature(entityTemperature);
    }

    public DtoTemperature getCurrentTemperature() {
        return mapperTemperature.mapToDtoTemperature(batteryRepository.getCurrentTemperature());
    }

    public void setTemperature(DtoTemperature degree) {
        EntityTemperature entityTemperature = mapperTemperature.mapToEntityTemperature(degree);
        batteryRepository.setTemperature(entityTemperature);
    }
}
