package com.config;

import com.entity.PaymentInfo;
import com.service.PaymentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sanjaya on 12/4/16.
 */
@Component
@Profile("test")
public class TestMessageListener {

    private static final Log log = LogFactory.getLog(TestMessageListener.class);

    private CountDownLatch countDownLatch;

    public AtomicInteger count = new AtomicInteger(0);

    @Autowired
    PaymentService paymentService;

    @RabbitListener(queues = "payment")
    public void receiveMessageQueueOne(PaymentInfo paymentInfo) {
        log.info("Received data : " + paymentInfo);
        count.incrementAndGet();
        paymentService.processPayment(paymentInfo);
        countDownLatch.countDown();
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
}
