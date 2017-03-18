package test;

import com.baturu.util.kafka.bean.Message;
import com.baturu.util.kafka.constants.TopicType;
import com.baturu.util.kafka.producer.Producer;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by fire on 15/7/26.
 */
public class TestProducer {

    @Test
    public void produceMsg() throws Exception {
        int i = 1;
        while(i < 3) {
            long time = new Date().getTime();
            Message message = new Message();
            message.setId(time + "");
            message.setUserId(531000112066L);
            message.setType(TopicType.TEST);

            message.setContent("徐燃在测试消息发送!时间:" + LocalDateTime.now().toString());
            message.setCreateTime(new Date().getTime());

            Producer producer = new Producer();
            producer.send(message);
            i++;
            Thread.sleep(1000);
        }
    }
}
