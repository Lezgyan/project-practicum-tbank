package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.dto.internal.DtoTimeBlind;
import ru.tbank.practicum.service.BlindService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blind")
public class BlindController {

    private final BlindService blindService;

    @PostMapping("/{id}/open")
    public void openBlind(@PathVariable Long id) {
        blindService.openBlind(id);
    }

    @PostMapping("/{id}/close")
    public void closeBlind(@PathVariable Long id) {
        blindService.closeBlind(id);
    }

    @PostMapping("/{id}/setOpeningAndClosingTime")
    public void setOpeningAndClosingTime(@PathVariable Long id, @Valid @RequestBody DtoTimeBlind dtoTimeBlind) {
        blindService.setOpeningAndClosingTime(id, dtoTimeBlind);
    }
}
