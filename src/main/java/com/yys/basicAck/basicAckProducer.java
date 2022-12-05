package com.yys.basicAck;

import com.rabbitmq.client.Channel;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class basicAckProducer {
    private static final String BASIC_ACK_QUEUE_NAME ="basicAckQueue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        //队列持久化
        boolean durable = true;
        channel.queueDeclare(BASIC_ACK_QUEUE_NAME,durable,false,false,null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", BASIC_ACK_QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完成：" + message);
        }
    }
}
