package ru.tbank.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.entity.DeviceState;

public interface DeviceStateRepository extends JpaRepository<DeviceState, Long> {}
