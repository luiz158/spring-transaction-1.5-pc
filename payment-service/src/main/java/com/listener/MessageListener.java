package com.listener;

import com.entity.PaymentInfo;
import com.service.PaymentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
/**
 * Created by sanjaya on 12/4/16.
 */
@Component
@Profile("!test")
public class MessageListener {

    private static final Log log = LogFactory.getLog(MessageListener.class);


    @Autowired
    PaymentService paymentService;

    @RabbitListener(queues = "payment")
    public void receiveMessageQueueOne(PaymentInfo paymentInfo) {
        log.info("Received data : " + paymentInfo);
        paymentService.processPayment(paymentInfo);
    }

}
