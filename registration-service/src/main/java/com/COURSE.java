package com;

/**
 * Created by sanjaya on 12/4/16.
 */
public enum COURSE {

    COMPUTER_SCIENCE(900),
    MECHANICAL_ENGINEERING(1000),
    EEE(1100);

    private double fee;

    private COURSE(double fee) {
        this.fee = fee;
    }

    public double getFee() {
        return fee;
    }
}
