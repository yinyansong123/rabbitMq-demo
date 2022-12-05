package com.yys.workQueues;


import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkerConsumer {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        DeliverCallback deliverCallback = (consumerTag,delivery) ->{
            System.out.println("c1拿到请求");
            System.out.println((new String(delivery.getBody())));
        };
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("调用失败");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
