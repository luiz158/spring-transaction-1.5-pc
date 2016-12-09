package com.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.amqp.core.BindingBuilder.bind;

/**
 * Created by sanjaya on 12/4/16.
 */
@Configuration
@EnableRabbit
public class MessageConfiguration {


    @Value("${global-exchange}")
    private String globalExchange;

    @Value("${dead-letter-exchange-name}")
    private String deadLetterExchange;

    @Value("${payment-queue}")
    String paymentQueue;

    @Value("${payment-dead-letter-queue}")
    String paymentDeadLetterQueue;


    @Bean
    DirectExchange globalExchange() {
        return new DirectExchange(globalExchange);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchange);
    }

    @Bean
    Queue paymentQueue() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", deadLetterExchange);
        args.put("x-dead-letter-routing-key", paymentDeadLetterQueue);
        return new Queue(paymentQueue, false, false, true, args);
    }

    @Bean
    Queue paymentDeadLetterQueue() {
        return new Queue(paymentDeadLetterQueue, false, false, true);
    }

    @Bean
    Binding bindingPaymentQueue(Queue paymentQueue, DirectExchange globalExchange) {
        return bind(paymentQueue).to(globalExchange).with(this.paymentQueue);
    }

    @Bean
    Binding bindingPaymentDeadLetter(Queue paymentDeadLetterQueue, DirectExchange deadLetterExchange) {
        return bind(paymentDeadLetterQueue).to(deadLetterExchange).with(this.paymentDeadLetterQueue);
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory cachingConnectionFactory,
                                                                               MessageConverter jsonMessageConverter,
                                                                               RetryOperationsInterceptor retryOperationsInterceptor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setChannelTransacted(true);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(retryOperationsInterceptor);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

    @Bean
    public RetryOperationsInterceptor statelessRetryInterceptorBuilder() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000) // initialInterval, multiplier, maxInterval
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
