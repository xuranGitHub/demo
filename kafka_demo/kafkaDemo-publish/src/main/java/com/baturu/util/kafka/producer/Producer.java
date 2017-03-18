package com.baturu.util.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.baturu.util.kafka.bean.Message;
import com.baturu.util.PropertiesUtil;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class Producer {

    private kafka.javaapi.producer.Producer inner;

    public Producer() {
        try {
            ProducerConfig config = new ProducerConfig(PropertiesUtil.getProperties("producer.properties"));
            this.inner = new kafka.javaapi.producer.Producer(config);
        } catch (Exception e) {
            log.error("消息推送时读取配置文件出错,{}", e.getMessage());
        }
    }

    /**
     * 发送一条消息
     *
     * @author fire
     * @date 15/7/25
     * @param message 消息实体
     */
    public boolean send(Message message) {
        message.setId(UUID.randomUUID().toString());
        String topicType = message.getType();
        try {
            if("".equals(topicType)){
                return false;
            }
            String msgStr = JSON.toJSONString(message);
            KeyedMessage<String, String> km = new KeyedMessage<>(topicType, msgStr); //如果具有多个partitions,请使用new KeyedMessage(String topicName, K key, V value).
            inner.send(km);
            log.info("发送消息成功:{}", msgStr);
            return true;
        } catch (Exception e) {
            log.error("发送消息失败:{}", e.getMessage());
            return false;
        } finally {
            inner.close();
        }
    }

    /**
     * 发送多条消息
     *
     * @author fire
     * @date 15/7/25
     * @param messageList 消息集合
     */
    public boolean send(List<Message> messageList) {
        try {
            if(messageList.isEmpty()){
                return false;
            }
            List<KeyedMessage<String, String>> kms = new ArrayList<>();
            for (Message message : messageList) {
                message.setId(UUID.randomUUID().toString());
                String topicType = message.getType();
                if("".equals(topicType)){
                    return false;
                }
                String msgStr = JSON.toJSONString(message);
                KeyedMessage<String, String> km = new KeyedMessage<>(topicType, msgStr);
                kms.add(km);
            }
            inner.send(kms);
            log.info("发送消息成功:{}", messageList.toString());
            return true;
        } catch (Exception e) {
            log.error("发送消息失败:{}", e.getMessage());
            return false;
        } finally {
            inner.close();
        }
    }

}