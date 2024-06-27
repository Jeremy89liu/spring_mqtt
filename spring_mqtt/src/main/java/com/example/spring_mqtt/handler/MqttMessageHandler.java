package com.example.spring_mqtt.handler;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class MqttMessageHandler {
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        String payload = (String) message.getPayload();
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        // 对接收到的消息进行处理，这里仅为打印示例
        System.out.println("Received message on topic '" + topic + "': " + payload);
    }
}
