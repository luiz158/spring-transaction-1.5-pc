package com.config;

import com.entity.PaymentInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by sanjaya on 12/4/16.
 */
@Component
public class TestListener {


    private CountDownLatch latch;

    private PaymentInfo paymentInfo;

    @RabbitListener(queues = "payment")
    public void receiveMessage(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
        latch.countDown();
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}
