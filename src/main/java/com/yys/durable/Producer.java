package com.yys.durable;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
public class Producer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        //开始发布确认
        channel.confirmSelect();
        String message = "Hello Word";
        //创建一个队列
        //1.队列名称
        //2.队列里面的消息是否持久化 也就是是否用完就删除
        //3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
        //4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
        //5.其他参数
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        //发送一个消息
        //1.发送到那个交换机
        //2.路由的 key 是哪个
        //3.其他的参数信息  (MessageProperties.MINIMAL_PERSISTENT_BASIC 消息持久化)
        //4.发送消息的消息体
        channel.basicPublish("", QUEUE_NAME, MessageProperties.MINIMAL_PERSISTENT_BASIC, message.getBytes());
        System.out.println("消息已发送");
    }
}
