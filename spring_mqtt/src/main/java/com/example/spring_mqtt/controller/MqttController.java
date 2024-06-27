package com.example.spring_mqtt.controller;

import com.example.spring_mqtt.service.SomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class MqttController {


    @Autowired
    private SomeService someService;

    @GetMapping("/send")
    public String send() {
        someService.sendMessageToTopicA("hello xiaoxiao");
        return "SUCCESS";
    }

}
