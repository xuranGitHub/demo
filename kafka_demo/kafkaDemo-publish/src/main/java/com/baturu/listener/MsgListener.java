package com.baturu.listener;

import com.baturu.util.PropertiesUtil;
import com.baturu.util.kafka.consumer.Consumer;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 消息接收监听器
 * 需要在web.xml加入以下配置
 <listener>
 <listener-class>com.qmsk.business.manage.msgPush.MsgListener</listener-class>
 </listener>
 *
 * @author fire
 * @date 15/8/17.
 */
public class MsgListener implements ServletContextListener {

    private final static Logger log = Logger.getLogger(MsgListener.class);

//    private Push push;
    private Consumer consumer;
    private List<Consumer> consumerList = new ArrayList<>();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (consumer != null) {
            consumer.shutdown();
        }

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        // 从配置文件读取需要接收的topic
        Map<String, String> propertiesMap = PropertiesUtil.getPropertiesMap("consumer.properties");
        String topics = propertiesMap.get("consumer_toppic");
        if (topics != null) {
            final String[] topicArr = topics.replaceAll("\\s+", "").split(",");
            consumer = new Consumer();
            consumer.getTopicListMessage(Arrays.asList(topicArr), 1);
        } else {
            log.error("消息监听器启动异常！consumer.properties配置文件没有指定需要接收的消息类型, 请通过consumer_toppic参数指定");
        }
    }

}
