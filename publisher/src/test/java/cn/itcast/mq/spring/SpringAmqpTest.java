package cn.itcast.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
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
        rabbitTemplate.convertAndSend(exchangeName,"red", msg);
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


}
