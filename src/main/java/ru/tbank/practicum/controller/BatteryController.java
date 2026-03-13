package ru.tbank.practicum.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("battery")
public class BatteryController {

    @PostMapping("/increaseTemperature")
    public void increaseTemperature(){

    }

    @PostMapping("/reduceTemperature")
    public void reduceTemperature(){

    }

}
