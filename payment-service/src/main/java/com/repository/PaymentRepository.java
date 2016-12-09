package com.repository;

import com.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sanjaya on 12/4/16.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
