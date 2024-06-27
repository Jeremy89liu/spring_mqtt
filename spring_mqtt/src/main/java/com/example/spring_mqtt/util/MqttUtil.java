package com.example.spring_mqtt.util;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttUtil {

    @Autowired(required = false)
    private MqttClient client;

    /**
     * 订阅主题
     * @param topic
     * @param qos
     */
    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    /**
     * 订阅主题
     * @param topic
     */
    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发布消息
     *
     * @param qos         连接方式 0,1,2 默认0
     *
     * @param retained    是否保留最新的消息
     * @param topic       订阅主题
     * @param pushMessage 消息体
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) throws MqttException {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mqttTopic = client.getTopic(topic);
        if(!client.isConnected()){
            client.connect();
        }
        if (null == mqttTopic) {
            log.error("topic not exist");
        }
        MqttDeliveryToken token;
        try {
            token = mqttTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布消息
     * @param topic 主题
     * @param pushMessage 消息内容
     */
    public void publish( String topic, String pushMessage) {
        try {
            publish(0,true,topic,pushMessage);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

}
