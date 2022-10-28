package com.websystique.springboot.model;

import java.math.BigDecimal;

public class BigDemInterfaceRate {
    private String date;
    private BigDecimal rate;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
