package com.baturu.util.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.baturu.util.PropertiesUtil;
import com.baturu.util.kafka.bean.Message;
import com.baturu.util.webSocket.MyWebSocket;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {

    private final static Logger log = Logger.getLogger(PropertiesUtil.class);

    private ConsumerConnector consumerConnector;
    private String topic;
    private List<String> topicList;
    private ExecutorService executor;

    private Push push;

    public void setPush(Push push) {
        this.push = push;
    }

    /**
     * 从配置文件读取配置信息
     * @author fire
     * @date 15/7/25
     * @return
     */
    private ConsumerConfig createConsumerConfig(){
        return new ConsumerConfig(PropertiesUtil.getProperties("consumer.properties"));
    }

    /**
     * 读取消息
     * @author fire
     * @date 15/7/25
     * @param topicType 消息类型
     * @param numThreads 需要多少个线程来读取
     */
    public void getMessage(String topicType, int numThreads) {
        this.consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.topic = topicType;

        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, new Integer(numThreads));  //读取哪个topic，需要几个线程读
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        for (final KafkaStream stream : streams) {
                    try {
                        ConsumerIterator<byte[], byte[]> it = stream.iterator();
                        while (it.hasNext()) {
                            String result = new String(it.next().message(), "UTF-8");
                            // 收到的消息转成Message对象
                            Message message = JSON.parseObject(result, Message.class, Feature.SortFeidFastMatch);
                            //push.pushMsg(message);
                            System.out.println(message);
                        }
                    } catch (UnsupportedEncodingException e) {
                        log.error("消息解码错误," + e.getMessage());
                    }
        }
    }

    /**
     * 根据指定的topic列表读取消息
     *
     * @author fire
     * @date 15/7/25
     * @param topicTypeList 消息类型列表
     * @param numThreads 需要多少个线程来读取
     */
    public void getTopicListMessage(List<String> topicTypeList, int numThreads) {
        this.consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.topicList = topicTypeList;
        Map<String, Integer> topicCountMap = new HashMap<>();
        for (String topic : topicList) {
            topicCountMap.put(topic, new Integer(numThreads));  //读取哪个topic，需要几个线程读
        }
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        executor = Executors.newFixedThreadPool(numThreads * topicList.size());
        for (String topic : topicList) {
            List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
            for (final KafkaStream stream : streams) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ConsumerIterator<byte[], byte[]> it = stream.iterator();
                            while (it.hasNext()) {
                                String result = new String(it.next().message(), "UTF-8");
                                // 收到的消息转成Message对象
                                Message message = JSON.parseObject(result, Message.class, Feature.SortFeidFastMatch);
                                //push.pushMsg(message);
                                MyWebSocket.sendMsg(message);
                            }
                        } catch (UnsupportedEncodingException e) {
                            log.error("消息解码错误," + e.getMessage());
                        }
                    }
                });
            }
        }
        if (executor.isShutdown()) {
            log.error("线程池关闭");
        }
    }

    public void shutdown() {
        try {
            executor.shutdown();
        } catch (Exception e) {
            log.error("线程池关闭异常");
        } finally {
            consumerConnector.shutdown();
        }
    }

}