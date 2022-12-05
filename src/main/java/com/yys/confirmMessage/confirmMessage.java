package com.yys.confirmMessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.yys.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认
 */
public class confirmMessage {
    public static final int MESSAGE_MAX_SIZE = 1000;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        //1.单次确认
//        ConfirmSinge();
        //2.批量确认
//        ConfirmBatch();
        //3.异步确认
        ConfirmAsync();
    }

    /**
     * 单次确认
     */
    public static void ConfirmSinge() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        String queueName = UUID.randomUUID().toString();
        //开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName,true,false,false,null);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_MAX_SIZE; i++) {
            String message = i+"";
            System.out.println("发送第"+message+"条消息");
            channel.basicPublish("",queueName,null,message.getBytes());
            channel.waitForConfirms();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("打印了"+MESSAGE_MAX_SIZE+"条消息，耗时为"+(endTime-startTime)+"ms");
    }

    /**
     * 批量确认
     */
    public static void ConfirmBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        String queueName = UUID.randomUUID().toString();
        //开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName,true,false,false,null);
        long startTime = System.currentTimeMillis();
        int confirmBatchCount = 0 ;
        for (int i = 0; i < MESSAGE_MAX_SIZE; i++) {
            String message = i+"";
            System.out.println("发送第"+message+"条消息");
            channel.basicPublish("",queueName,null,message.getBytes());
            confirmBatchCount++;
            if (confirmBatchCount == 100){
                confirmBatchCount =0;
                System.out.println("当前i为"+i);
                channel.waitForConfirms();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("打印了"+MESSAGE_MAX_SIZE+"条消息，耗时为"+(endTime-startTime)+"ms");
    }

    /**
     * 异步确认
     */
    public static void ConfirmAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.RabbitMqConnectByLocal();
        String queueName = UUID.randomUUID().toString();
        //开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(queueName,true,false,false,null);
        long startTime = System.currentTimeMillis();

        //成功回调
        //1.deliveryTag 消息的标记
        //2.multiple 是否批量
        ConfirmCallback ackCallback =  (deliveryTag, multiple) ->{
            System.out.println("消息确认成功"+deliveryTag);
        };
        //未确认回调
        ConfirmCallback nackCallback =(deliveryTag, multiple) ->{
            System.out.println("未成功确认"+deliveryTag);
        };
        //发布确认监听器
        channel.addConfirmListener(ackCallback,nackCallback);
        for (int i = 0; i < MESSAGE_MAX_SIZE; i++) {
            String message = i+"";
            System.out.println("发送第"+message+"条消息");
            channel.basicPublish("",queueName,null,message.getBytes());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("打印了"+MESSAGE_MAX_SIZE+"条消息，耗时为"+(endTime-startTime)+"ms");
    }
}
