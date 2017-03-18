package com.baturu.util.kafka.bean;

import com.baturu.util.kafka.constants.TopicType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * Created by xuran on 15/7/25.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * 消息Id
     */
    private String id;

    /**
     * 用户Id
     */
    private long userId;

    /**
     * 发送方sessionId
     */
    private String senderSessionId;

    /**
     * 消息类型
     * @see TopicType
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 点击后的链接
     */
    private String url;

    /**
     * 消息创建时间
     */
    private long createTime;

}
