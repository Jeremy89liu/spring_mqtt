package com.example.spring_mqtt.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Data
@Slf4j
public class MqttConfig {

//    String host;
//    String clientId;
//    String topic;
//    String username;
//    String password;
//    Integer timeout;
//    Integer keepalive;
//
//    @Bean
//    @ConditionalOnProperty(prefix = "mqtt",name="open",havingValue = "true")
//    public MqttConnectOptions mqttConnectOptions() {
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setUserName(username);
//        options.setPassword(password.toCharArray());
//        options.setCleanSession(true);
//        options.setConnectionTimeout(timeout);
//        options.setKeepAliveInterval(keepalive);
//        return options;
//    }
//
//    @Bean
//    @ConditionalOnProperty(prefix = "mqtt",name="open",havingValue = "true")
//
//    public MqttClient mqttClient(MqttConnectOptions mqttConnectOptions) {
//        try {
//            MqttClient client = new MqttClient(host, clientId);
//
//            client.setCallback(new MessageCallback());
//            IMqttToken iMqttToken = client.connectWithResult(mqttConnectOptions);
//            boolean complete = iMqttToken.isComplete();
//            log.info("mqtt建立连接：{}", complete);
//
//            // 订阅主题
//            client.subscribe(topic, 0);
//            log.info("已订阅topic：{}", topic);
//
//            return client;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("mqtt 连接异常");
//        }
//    }

    private String brokerUrl;
    private String clientId;
    private String userName;

    private String password;

    private List<String> subscriptionTopics;

    private List<String> publishTopics;

    @Bean
    public DefaultMqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setUserName(userName);
        options.setPassword(password.toCharArray());

        // 设置其他连接选项，如用户名、密码等
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mqttOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInboundAdapter(DefaultMqttPahoClientFactory factory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId + "-subscriber", factory, subscriptionTopics.toArray(new String[0]));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttOutputChannel")
    public MqttPahoMessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId + "-publisher", factory);
        handler.setDefaultTopic(publishTopics.get(0)); // 如果所有发布消息都发往同一个默认主题
        // 或者使用自定义策略，例如：
        handler.setTopicExpression(new SpelExpressionParser().parseExpression("headers['mqtt_topic']")); // 动态获取主题
        handler.setAsync(true); // 设置为异步发布
        return handler;
    }
}