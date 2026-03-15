package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.dto.internal.DtoTemperature;
import ru.tbank.practicum.service.BatteryService;

@RestController
@RequestMapping("/battery")
public class BatteryController {

    private final BatteryService batteryService;

    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @PostMapping("/increaseTemperature")
    public void increaseTemperature(@Valid @RequestBody DtoTemperature dtoTemperature) {
        batteryService.upTemperature(dtoTemperature);
    }

    @PostMapping("/reduceTemperature")
    public void reduceTemperature(@Valid @RequestBody DtoTemperature dtoTemperature) {
        batteryService.downTemperature(dtoTemperature);
    }

    @GetMapping("/getCurrentTemperature")
    public DtoTemperature getTemperature() {
        return batteryService.getCurrentTemperature();
    }

    @PostMapping("/setCurrentTemperature")
    public void setTemperature(@Valid @RequestBody DtoTemperature dtoTemperature) {
        batteryService.setTemperature(dtoTemperature);
    }
}
