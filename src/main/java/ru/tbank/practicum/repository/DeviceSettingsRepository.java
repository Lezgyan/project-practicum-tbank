package ru.tbank.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.entity.DeviceSettings;

public interface DeviceSettingsRepository extends JpaRepository<DeviceSettings, Long> {
    DeviceSettings findByDeviceId(Long deviceId);
}
