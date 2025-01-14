package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 简单队列
     */
    @Test
    public void testSimpleQueue(){
        String queueName = "simple.queue";
        String msg = "hello msg";
        rabbitTemplate.convertAndSend(queueName, msg);
    }

    /**
     * workQueue
     * 向队列中不停发送消息，模拟消息堆积。
     */
    @Test
    public void testWorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        String msg = "hello msg ";

        for (int i = 1; i <= 50; i++) {
            rabbitTemplate.convertAndSend(queueName, msg + i);
             Thread.sleep(20);
        }
    }

    /**
     * 发布订阅 - 广播队列
     * @throws InterruptedException
     */
    @Test
    public void testFanoutQueue() throws InterruptedException {
        String exchangeName = "hmall.fanout";
        String msg = "hello all ";
        rabbitTemplate.convertAndSend(exchangeName,"", msg);
    }

    /**
     * 发布订阅 - 路由模式
     * @throws InterruptedException
     */
    @Test
    public void testDirectQueue() throws InterruptedException {
        // 队列名称
        String exchangeName = "hmall.direct";
        String routingKey = "red";
        // 消息
        String msg = "hello 大家好: " + routingKey;
        // 发送消息，参数依次为：交换机名称，RoutingKey，消息
        rabbitTemplate.convertAndSend(exchangeName,routingKey, msg);
    }

    /**
     * 发布订阅 - 主题模式
     * @throws InterruptedException
     */
    @Test
    public void testTopicQueue() throws InterruptedException {
        // 队列名称
        String exchangeName = "hmall.topic";
        // 消息
        String msg = "11111";
        // 发送消息，参数依次为：交换机名称，RoutingKey，消息
        rabbitTemplate.convertAndSend(exchangeName,"china.aaa", msg);
    }

    @Test
    public void testSendMap() throws InterruptedException {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "Rose");
        msg.put("age", 29);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    @Test
    public void testPublisherConfirm() throws InterruptedException {
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData();
        // 2.给Future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                if(result.isAck()){ // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    log.debug("发送消息成功，收到 ack!");
                }else{ // result.getReason()，String类型，返回nack时的异常描述
                    log.error("发送消息失败，收到 nack, reason : {}", result.getReason());
                }
            }
        });
        // 3.发送消息
        rabbitTemplate.convertAndSend("hmall.direct111", "blue11", "hello", cd);
        // 单元测试结束，jvm就退出了，此时没有时间去等待回调函数执行完毕，所以需要等待一段时间
        Thread.sleep(60000);
    }

    @Test
    public void testSimpleMessage(){
//        String queueName = "simple.queue";
        String queueName = "lazy.queue";
        Message message = MessageBuilder.withBody("hello world".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build();
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend(queueName, message);
        }

    }

    @Test
    public void testRetry() throws InterruptedException {
        // 队列名称
        String exchangeName = "error.direct";
        String routingKey = "error";
        // 消息
        String msg = "hello 大家好: " + routingKey;
        // 发送消息，参数依次为：交换机名称，RoutingKey，消息
        rabbitTemplate.convertAndSend(exchangeName,routingKey, msg);
    }

    /**
     * 测试消息的死信队列
     * @throws InterruptedException
     */
    @Test
    public void testDeadLetterQueue() throws InterruptedException {
        log.info("发送消息");
        rabbitTemplate.convertAndSend("normal.direct", "hi", "测试一下。。。", message -> {
            message.getMessageProperties().setExpiration("10000");
            return message;
        });
    }

    /**
     * 测试消息的延迟队列
     * @throws InterruptedException
     */
    @Test
    public void testDelayQueue() throws InterruptedException {
        log.info("发送消息");
        rabbitTemplate.convertAndSend("delay.direct", "delay", "测试一下。。。", message -> {
            message.getMessageProperties().setDelay(10000);
            return message;
        });
    }

}
