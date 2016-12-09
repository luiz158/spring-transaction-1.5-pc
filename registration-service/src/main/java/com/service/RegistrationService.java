package com.service;

import com.entity.PaymentInfo;
import com.entity.Registration;
import com.producer.MessageProducer;
import com.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.COURSE.COMPUTER_SCIENCE;

/**
 * Created by sanjaya on 12/4/16.
 */
@Service
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    MessageProducer<PaymentInfo> messageProducer;

    @Value("${global-exchange}")
    private String globalExchange;

    @Value("${payment-queue}")
    String paymentQueue;

    @Transactional
    public Registration doRegistration(Registration registration) {
        registration = registrationRepository.save(registration);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setRegistrationId(registration.getId());
        paymentInfo.setFee(COMPUTER_SCIENCE.getFee());

        messageProducer.publish(globalExchange, paymentQueue, paymentInfo);
        registration.setPaymentInfo(paymentInfo);
        return registration;
    }
}
