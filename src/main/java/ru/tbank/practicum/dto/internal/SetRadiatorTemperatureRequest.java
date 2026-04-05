package ru.tbank.practicum.dto.internal;

import jakarta.validation.constraints.NotNull;

public record SetRadiatorTemperatureRequest(@NotNull Double temperature) {}
