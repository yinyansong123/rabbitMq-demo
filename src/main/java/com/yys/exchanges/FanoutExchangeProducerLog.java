package com.yys.exchanges;

import com.rabbitmq.client.Channel;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutExchangeProducerLog {
    public static final String FANOUT_EXCHANGE_NAME = "fanoutExchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        channel.basicPublish(FANOUT_EXCHANGE_NAME,"",null,"我是一条日志".getBytes());
    }
}
