package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.dto.internal.DtoTemperature;
import ru.tbank.practicum.service.BatteryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/battery")
public class BatteryController {

    private final BatteryService batteryService;

    @PostMapping("/{id}/increaseTemperature")
    public void increaseTemperature(@PathVariable Long id, @Valid @RequestBody DtoTemperature dtoTemperature) {
        batteryService.upTemperature(id, dtoTemperature);
    }

    @PostMapping("/{id}/reduceTemperature")
    public void reduceTemperature(@PathVariable Long id, @Valid @RequestBody DtoTemperature dtoTemperature) {
        batteryService.downTemperature(id, dtoTemperature);
    }

    @GetMapping("/{id}/getCurrentTemperature")
    public DtoTemperature getTemperature(@PathVariable Long id) {
        return batteryService.getCurrentTemperature(id);
    }

    @PostMapping("/{id}/setCurrentTemperature")
    public void setTemperature(@PathVariable Long id, @Valid @RequestBody DtoTemperature dtoTemperature) {
        batteryService.setTemperature(id, dtoTemperature);
    }
}
