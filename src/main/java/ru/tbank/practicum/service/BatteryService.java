package ru.tbank.practicum.service;

import org.springframework.stereotype.Service;
import ru.tbank.practicum.dto.internal.DtoTemperature;
import ru.tbank.practicum.repository.BatteryRepository;

@Service
public class BatteryService {

    private final BatteryRepository batteryRepository;

    public BatteryService(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    public void upTemperature(Long id, DtoTemperature dtoTemperature) {
        batteryRepository.upTemperature(id, dtoTemperature.degree());
    }

    public void downTemperature(Long id, DtoTemperature dtoTemperature) {
        batteryRepository.downTemperature(id, dtoTemperature.degree());
    }

    public DtoTemperature getCurrentTemperature(Long id) {
        return new DtoTemperature(batteryRepository.getCurrentTemperature(id).getDegree());
    }

    public void setTemperature(Long id, DtoTemperature degree) {
        batteryRepository.setTemperature(id, degree.degree());
    }
}
