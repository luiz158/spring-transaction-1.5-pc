package com.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by sanjaya on 12/4/16.
 */
@Entity
@Table(name = "PAYMENT")
public class Payment {


    @Id
    @SequenceGenerator(name = "paymentSeq", sequenceName = "PAYMENT_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "paymentSeq")
    @Column(name = "PAYMENT_ID")
    private Long id;


    @Column(name = "REG_ID", nullable = false)
    private Long registrationId;


    @Column(name = "REG_FEE", nullable = false)
    private double fee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
