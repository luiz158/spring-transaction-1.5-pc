package com.service;

import com.entity.Payment;
import com.entity.PaymentInfo;
import com.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sanjaya on 12/4/16.
 */
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public Payment processPayment(PaymentInfo paymentInfo) {
        Payment payment = new Payment();
        payment.setRegistrationId(paymentInfo.getRegistrationId());
        payment.setFee(paymentInfo.getFee());
        return paymentRepository.save(payment);
    }
}
