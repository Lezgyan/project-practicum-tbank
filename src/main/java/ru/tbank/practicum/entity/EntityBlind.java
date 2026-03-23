package ru.tbank.practicum.entity;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EntityBlind {
    private Long id;
    private StateBlind state;
    private Instant openingTime;
    private Instant closingTime;
}
