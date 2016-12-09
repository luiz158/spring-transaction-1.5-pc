package com.service;

import com.COURSE;
import com.RegistrationApplication;
import com.config.TestListener;
import com.entity.Registration;
import com.repository.RegistrationRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * Created by sanjaya on 12/4/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RegistrationApplication.class)
public class RegistrationServiceTest {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    TestListener testListener;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Before
    public void setUp(){
        registrationRepository.deleteAll();
        rabbitAdmin.purgeQueue("payment", false);
        rabbitAdmin.purgeQueue("payment-dead-letter-queue", false);
    }

    @Test
    public void registrationSuccessfulHappyScenario() throws InterruptedException {

        // given
        Registration registration = new Registration();
        registration.setCourse(COURSE.COMPUTER_SCIENCE);
        registration.setName("Allen");
        testListener.setLatch(new CountDownLatch(1));

        // then
        Registration expected = registrationService.doRegistration(registration);
        while (testListener.getLatch().getCount() != 0) {}

        Assert.assertNotNull(expected.getId());
        assertEquals(expected.getPaymentInfo().getFee(), COURSE.COMPUTER_SCIENCE.getFee(), 0.001);
        assertEquals(expected.getId(), expected.getPaymentInfo().getRegistrationId());

        // asserting messaging part
        assertEquals(testListener.getPaymentInfo().getRegistrationId(), expected.getPaymentInfo().getRegistrationId());
        assertEquals(testListener.getPaymentInfo().getFee(), expected.getPaymentInfo().getFee(), 0.001);
    }
}
