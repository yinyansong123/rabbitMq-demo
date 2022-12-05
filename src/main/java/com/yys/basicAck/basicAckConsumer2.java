package com.yys.basicAck;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class basicAckConsumer2 {
    private static final String BASIC_ACK_QUEUE_NAME ="basicAckQueue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        //不公平分发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        DeliverCallback deliverCallback = (comTag,delivery) ->{
            String message =new String(delivery.getBody());
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("c2接收到消息"+message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = comTag  ->{
            System.out.println("c1消费失败");
        };


        boolean autoACK = false;
        channel.basicConsume(BASIC_ACK_QUEUE_NAME,autoACK,deliverCallback,cancelCallback);
    }
}
