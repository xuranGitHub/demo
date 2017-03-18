package test;

import com.baturu.util.kafka.consumer.Consumer;
import org.junit.Test;

/**
 * Created by xuran on 15/7/26.
 */
public class TestConsumer {


    @Test
    public void testWebConsumer() {
        Consumer consumer = new Consumer();
        consumer.getMessage("test", 1);
    }

}
