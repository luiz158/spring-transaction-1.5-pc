package com.service;

import com.COURSE;
import com.RegistrationApplication;
import com.config.TestListener;
import com.entity.Registration;
import com.repository.RegistrationRepository;
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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

/**
 * Created by sanjaya on 12/4/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RegistrationApplication.class)
public class RegistrationServiceTransactionTest {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    TestListener testListener;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Before
    public void setUp() {
        registrationRepository.deleteAll();
        rabbitAdmin.purgeQueue("payment", false);
        rabbitAdmin.purgeQueue("payment-dead-letter-queue", false);

    }

    @Test
    public void shouldNotMessageReceivedByTestListenerWhenDBFailureOccurred() throws InterruptedException {
        // given
        Registration registration = new Registration();
        registration.setCourse(COURSE.COMPUTER_SCIENCE);
        registration.setName("ALLEN ALLEN ALLEN ALLEN");
        testListener.setLatch(new CountDownLatch(1));

        // when
        Registration expected = null;
        try {
            expected = registrationService.doRegistration(registration);
        } catch (Exception e) {}

        // then
        Assert.assertNull(expected);
        // waiting 10 seconds and then check if message received
        testListener.getLatch().await(10, SECONDS);
        // assert that message not received
        assertEquals(1l, testListener.getLatch().getCount());
        assertEquals(0, registrationRepository.findAll().size());
    }

    @Test
    public void shouldNotPersistIntoDBWhenMessageDeliveryFailureOccurred() throws InterruptedException {
        // given
        Registration registration = new Registration();
        registration.setCourse(COURSE.COMPUTER_SCIENCE);
        registration.setName("ALLEN");
        testListener.setLatch(new CountDownLatch(1));

        // when
        Registration expected = null;
        // nullifying message converter, to simulate message publishing failure scenario
        rabbitTemplate.setMessageConverter(null);

        try {
            expected = registrationService.doRegistration(registration);
        } catch (Exception e) {
        }

        Assert.assertNull(expected);
        // waiting 10 seconds and then check if message received
        testListener.getLatch().await(10, SECONDS);
        // then
        // assert that message not received
        assertEquals(testListener.getLatch().getCount(), 1L);
        assertEquals(registrationRepository.findAll().size(), 0);
    }
}
