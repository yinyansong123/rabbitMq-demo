package com.yys.helloWord;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 */
public class Consumer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        System.out.println("等待接收消息.........");

        //成功回调，打印结果
        DeliverCallback deliverCallback = (consumerTag,delivery) ->{
            String message = new String(delivery.getBody());
            System.out.println(message);
        };

        //失败回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("接受消息中断");
        };


        //消费者消费消息 - 接受消息
        //1.消费哪个队列
        //2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
        //3.消费者成功消费的回调
        //4.消息被取消时的回调
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
