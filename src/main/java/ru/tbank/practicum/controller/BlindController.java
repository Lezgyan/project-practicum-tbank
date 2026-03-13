package ru.tbank.practicum.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blind")
public class BlindController {

    @PostMapping("/open")
    public void openBlind(){

    }

    @PostMapping("/close")
    public void closeBlind(){

    }

    @PostMapping("/setClosingTime")
    public void setClosingTime(){

    }

    @PostMapping("/setOpeningTime")
    public void setOpeningTime(){

    }
}
