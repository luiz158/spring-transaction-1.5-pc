package com.entity;

import com.COURSE;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by sanjaya on 12/4/16.
 */

@Entity
@Table(name = "REGISTRATION")
public class Registration {

    @Id
    @SequenceGenerator(name = "registrationSeq", sequenceName = "REGISTRATION_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "registrationSeq")
    @Column(name = "DEP_ID")
    private Long id;


    @Column(name = "STD_NAME", length = 10, nullable = false)
    private String name;


    @Enumerated(EnumType.STRING)
    @Column(name = "COURSE_NAME", length = 20, nullable = false)
    private COURSE course;

    @Transient
    private PaymentInfo paymentInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public COURSE getCourse() {
        return course;
    }

    public void setCourse(COURSE course) {
        this.course = course;
    }


    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}
