package ru.tbank.practicum.service;

import java.time.ZonedDateTime;
import ru.tbank.practicum.entity.Device;

public interface DeviceService {
    void apply(Device device, ZonedDateTime now);
}
