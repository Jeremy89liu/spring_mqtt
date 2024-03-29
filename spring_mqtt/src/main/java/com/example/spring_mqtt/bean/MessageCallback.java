package com.example.spring_mqtt.bean;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageCallback implements MqttCallback {

    @Autowired(required = false)
    private MqttClient client;

    @Override
    public void connectionLost(Throwable throwable) {
        if (client == null || !client.isConnected()) {
            log.info("连接断开，正在重连....");
            try {
                client.connect();
                if(client.isConnected()){
                    log.info("mqtt重连完成.");
                }
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("接收消息主题 : " + topic);
        log.info("接收消息内容 : " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
    }
}
