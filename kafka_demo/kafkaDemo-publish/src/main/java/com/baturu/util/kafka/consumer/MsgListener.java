package com.baturu.util.kafka.consumer;

import com.baturu.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息接收监听器
 * 需要在web.xml加入以下配置
 <listener>
 <listener-class>com.qmsk.business.manage.msgPush.MsgListener</listener-class>
 </listener>
 *
 * @author xuran
 * @date 15/8/17.
 */
public class MsgListener implements ServletContextListener {

    private final static Logger log = Logger.getLogger(MsgListener.class);

    private Push push;
    private ExecutorService executor;
    private Consumer consumer;
    private List<Consumer> consumerList = new ArrayList<>();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        for (Consumer consumer : consumerList) {
//            consumer.shutdown();
//        }
        if (consumer != null) {
            consumer.shutdown();
        }
        if (executor != null) {
            executor.shutdown();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        push = (Push) ctx.getBean("push");

        // 从配置文件读取需要接收的topic
        Map<String, String> propertiesMap = PropertiesUtil.getPropertiesMap("consumer.properties");
        String topics = propertiesMap.get("consumer_toppic");
        if (topics != null) {
            final String[] topicArr = topics.replaceAll("\\s+", "").split(",");
            consumer = new Consumer();
            consumer.setPush(push);
            executor = Executors.newCachedThreadPool();
            executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        consumer.getTopicListMessage(Arrays.asList(topicArr), 2);
                    }
                });
        } else {
            log.error("消息监听器启动异常！consumer.properties配置文件没有指定需要接收的消息类型, 请通过consumer_toppic参数指定");
        }
    }

}
