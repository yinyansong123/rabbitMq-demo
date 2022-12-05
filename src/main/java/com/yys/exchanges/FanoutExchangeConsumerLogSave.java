package com.yys.exchanges;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 扇形交换机
 */

public class FanoutExchangeConsumerLogSave {

    public static final String FANOUT_EXCHANGE_NAME = "fanoutExchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //生成一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,FANOUT_EXCHANGE_NAME,"");
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String msg = new String(message.getBody());
            System.out.println("把消息保存在磁盘中"+msg);
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        channel.basicConsume(queueName,false,deliverCallback,(consumerTag -> {
            System.out.println("失败返回");
        }));
    }
}
