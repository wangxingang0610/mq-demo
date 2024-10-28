package cn.itcast.mq.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringRabbitListener {

//    /**
//     * 1. 简单队列（消费者）
//     * @param msg
//     * @throws Exception
//     */
//    @RabbitListener(queues = "simple.queue")
//    public void listenerSimpleQueueMessage(String msg) throws Exception{
//        System.out.println("spring 消费者接收到消息 ：【" + msg + "】");
//    }
//    /*----------------------------------------------------------------------------------------*/
//    /**
//     * 工作队列（消费者）
//     * @param msg
//     * @throws Exception
//     */
//    @RabbitListener(queues = "work.queue")
//    public void listenerWorkQueueMessage1(String msg) throws Exception{
//        System.out.println("spring 消费者1接收到消息 ：【" + msg + "】");
//        Thread.sleep(20);
//    }
//
//    /**
//     * 工作队列（消费者）
//     * @param msg
//     * @throws Exception
//     */
//    @RabbitListener(queues = "work.queue")
//    public void listenerWorkQueueMessage2(String msg) throws Exception{
//        System.err.println("spring 消费者2接收到消息 ：【" + msg + "】");
//        Thread.sleep(500);
//    }
//
//    @RabbitListener(queues = "fanout.queue1")
//    public void listenerFanoutQueue1(String msg){
//        System.out.println("消费者1接收到Fanout消息：【" + msg + "】");
//    }
//    @RabbitListener(queues = "fanout.queue2")
//    public void listenerFanoutQueue2(String msg){
//        System.out.println("消费者2接收到Fanout消息：【" + msg + "】");
//    }
//
//    @RabbitListener(queues = "direct.queue1")
//    public void listenerDirectQueue1(String msg){
//        System.out.println("消费者1接收到direct消息：【" + msg + "】");
//    }
//    @RabbitListener(queues = "direct.queue2")
//    public void listenerDirectQueue2(String msg){
//        System.out.println("消费者2接收到direct消息：【" + msg + "】");
//    }
//
//    @RabbitListener(queues = "topic.queue1")
//    public void listenerTopicQueue1(String msg){
//        System.out.println("消费者1接收到topic.queue1消息：【" + msg + "】");
//    }
//    @RabbitListener(queues = "topic.queue2")
//    public void listenerTopicQueue2(String msg){
//        System.out.println("消费者2接收topic.queue2消息：【" + msg + "】");
//    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue1"),
            exchange = @Exchange(name = "hmall.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void listenerDirectQueue1(String msg) {
        System.out.println("消费者1接收到Direct消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue2"),
            exchange = @Exchange(name = "hmall.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void listenerDirectQueue2(String msg) {
        System.out.println("消费者2接收到Direct消息：【" + msg + "】");
    }


//
//    /*----------------------------------------------------------------------------------------*/

    /**
     * 发布订阅 - 路由模式
     * @param msg
     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "direct.queue1"),
//            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
//            key = {"red", "blue"}
//    ))
//    public void listenerDirectQueue1(String msg){
//        System.out.println("消费者1接收到Direct消息：【"+msg+"】");
//    }
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "direct.queue2"),
//            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
//            key = {"red", "yellow"}
//    ))
//    public void listenerDirectQueue2(String msg){
//        System.out.println("消费者2接收到Direct消息：【"+msg+"】");
//    }
//
//    /*----------------------------------------------------------------------------------------*/
//
//    /**
//     * 发布订阅 - 主体模式
//     * @param msg
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "topic.queue1"),
//            exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
//            key = "china.#"
//    ))
//    public void listenerTopicQueue1(String msg){
//        System.out.println("消费者1接收到Topic消息：【"+msg+"】");
//    }
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "topic.queue2"),
//            exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
//            key = "#.news"
//    ))
//    public void listenerTopicQueue2(String msg){
//        System.out.println("消费者2接收到Topic消息：【"+msg+"】");
//    }


//    @RabbitListener(queues = "object.queue")
//    public void listenObjectQueue(Map<String, Object> msg) {
//        System.out.println("收到消息：【" + msg + "】");
//    }

}
