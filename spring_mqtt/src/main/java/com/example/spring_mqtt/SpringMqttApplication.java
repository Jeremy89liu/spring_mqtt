package com.example.spring_mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringMqttApplication {
    @Autowired
    private MqttPahoMessageDrivenChannelAdapter mqttInboundAdapter;

    public static void main(String[] args) {
        SpringApplication.run(SpringMqttApplication.class, args);
    }

    @PostConstruct
    public void init() {
        mqttInboundAdapter.start();
    }
}
