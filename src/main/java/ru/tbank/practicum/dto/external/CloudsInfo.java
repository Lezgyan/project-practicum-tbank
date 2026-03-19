package ru.tbank.practicum.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloudsInfo(@JsonProperty("all") Integer cloudiness) {}
