package com.service;

import com.PaymentApplication;
import com.config.TestMessageListener;
import com.entity.PaymentInfo;
import com.repository.PaymentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

/**
 * Created by sanjaya on 12/4/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentApplication.class)
public class PaymentServiceTransactionalTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    TestMessageListener testMessageListener;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    MessageConverter messageConverter;

    @Before
    public void before() {
        paymentRepository.deleteAll();
        rabbitAdmin.purgeQueue("payment", false);
        rabbitAdmin.purgeQueue("payment-dead-letter-queue", false);
    }

    @Test
    public void shouldMessageRetriesConfiguredTimeAndPushIntoDeadLetterIfDBOperationsFail() throws InterruptedException {
        // given
        testMessageListener.setCountDownLatch(new CountDownLatch(1));
        PaymentInfo paymentInfo = new PaymentInfo();
        // value to fail DB operations
        paymentInfo.setRegistrationId(null);
        paymentInfo.setFee(10000);

        // when
        rabbitTemplate.convertAndSend("global-exchange", "payment", paymentInfo);
        testMessageListener.getCountDownLatch().await(10, SECONDS);

        Message message = rabbitTemplate.receive("payment-dead-letter-queue");
        PaymentInfo info = (PaymentInfo) messageConverter.fromMessage(message);

        assertEquals(paymentInfo.getRegistrationId(), info.getRegistrationId());
        assertEquals(paymentInfo.getFee(), paymentInfo.getFee(), 0.0001);
        assertEquals(1, testMessageListener.getCountDownLatch().getCount());
        assertEquals(3, testMessageListener.count.get());
        assertEquals(0, paymentRepository.findAll().size());

    }
}
