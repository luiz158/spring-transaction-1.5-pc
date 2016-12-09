package com.service;

import com.PaymentApplication;
import com.config.TestMessageListener;
import com.entity.Payment;
import com.entity.PaymentInfo;
import com.repository.PaymentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * Created by sanjaya on 12/4/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApplication.class)
public class PaymentServiceTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    TestMessageListener testMessageListener;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Before
    public void before() {
        paymentRepository.deleteAll();
        rabbitAdmin.purgeQueue("payment", false);
        rabbitAdmin.purgeQueue("payment-dead-letter-queue", false);

    }

    @Test
    public void testPaymentHappyScenario() throws InterruptedException {
        // given
        testMessageListener.setCountDownLatch(new CountDownLatch(1));
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setRegistrationId(99L);
        paymentInfo.setFee(10000);

        // when
        rabbitTemplate.convertAndSend("global-exchange", "payment", paymentInfo);
        while (testMessageListener.getCountDownLatch().getCount() != 0) {
        }

        // then
        Assert.assertEquals(0, testMessageListener.getCountDownLatch().getCount());
        Assert.assertEquals(1, paymentRepository.findAll().size());

        Payment payment = paymentRepository.findAll().get(0);
        Assert.assertEquals(payment.getFee(), paymentInfo.getFee(), 0.001);
        Assert.assertEquals(payment.getRegistrationId(), paymentInfo.getRegistrationId());
    }
}
