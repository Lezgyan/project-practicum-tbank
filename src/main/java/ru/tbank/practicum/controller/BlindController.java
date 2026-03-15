package ru.tbank.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.dto.internal.DtoTimeBlind;
import ru.tbank.practicum.service.BlindService;

@RestController
@RequestMapping("/blind")
public class BlindController {

    private final BlindService blindService;

    public BlindController(BlindService blindService) {
        this.blindService = blindService;
    }

    @PostMapping("/open")
    public void openBlind() {
        blindService.openBlind();
    }

    @PostMapping("/close")
    public void closeBlind() {
        blindService.closeBlind();
    }

    @PostMapping("/setClosingTime")
    public void setClosingTime(@Valid @RequestBody DtoTimeBlind dtoTimeBlind) {
        blindService.setClosingTime(dtoTimeBlind);
    }

    @PostMapping("/setOpeningTime")
    public void setOpeningTime(@Valid @RequestBody DtoTimeBlind dtoTimeBlind) {
        blindService.setOpeningTime(dtoTimeBlind);
    }
}
