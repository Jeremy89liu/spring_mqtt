package com.example.spring_mqtt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class SomeService {
    @Autowired
    private MessageChannel mqttOutputChannel;

    public void sendMessageToTopicA(String payload) {
        // 构建要发布的消息
        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader("mqtt_topic", "test")
                .build();
        // 将消息发布到mqttOutputChannel，从而触发MQTT消息发布
        mqttOutputChannel.send(message);
    }
}
