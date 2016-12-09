package com.entity;

/**
 * Created by sanjaya on 12/4/16.
 */
public class PaymentInfo {

    private Long registrationId;
    private double fee;

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public double getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "registrationId=" + registrationId +
                ", fee=" + fee +
                '}';
    }
}
