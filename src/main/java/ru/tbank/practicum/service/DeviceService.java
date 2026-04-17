package ru.tbank.practicum.service;

import ru.tbank.practicum.entity.Device;

public interface DeviceService {
    default void beforeBatch() {}

    void apply(Device device);

    default void afterBatch() {}
}
