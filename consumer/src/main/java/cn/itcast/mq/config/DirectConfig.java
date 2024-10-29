package cn.itcast.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {
    /**
     * 声明DirectExchange交换机
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        // 方式1
        // return ExchangeBuilder.directExchange("hmall.direct").build();
        // 方式2
        return new DirectExchange("hmall.direct");
    }

    /**
     * 声明第1个队列
     *
     * @return
     */
    @Bean
    public Queue directQueue1() {
        // QueueBuilder.durable("direct.queue1").build();
        return new Queue("direct.queue1");
    }

    /**
     * 绑 定队列1和交换机
     *
     * @param directQueue1
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindingQueueWithRed(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("red");
    }

    @Bean
    public Binding bindingQueueWithBlue(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("blue");
    }

    /**
     * 声明第1个队列
     *
     * @return
     */
    @Bean
    public Queue directQueue2() {
        return new Queue("direct.queue2");
    }

    /**
     * 绑 定队列1和交换机
     *
     * @param directQueue2
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindingQueue2WithRed(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("red");
    }

    @Bean
    public Binding bindingQueue2WithYellow(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("yellow");
    }

    @Bean
    public DirectExchange errorMessageExchange(){
        return new DirectExchange("error.direct");
    }
    @Bean
    public Queue errorQueue(){
        return new Queue("error.queue", true);
    }
    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange){
        return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with("error");
    }

    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }


}
