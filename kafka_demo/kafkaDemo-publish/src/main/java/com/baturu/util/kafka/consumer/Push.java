package com.baturu.util.kafka.consumer;

import com.baturu.util.kafka.bean.Message;

/**
 * Created by xuran on 15/7/26.
 */
public interface Push {

    void pushMsg(Message message);

}
