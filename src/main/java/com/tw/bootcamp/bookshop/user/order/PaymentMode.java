package com.tw.bootcamp.bookshop.user.order;

public enum PaymentMode {
    CREDIT_CARD("CREDIT_CARD"),
    CASH_ON_DELIVERY("CASH_ON_DELIVERY");


    private String paymentMode;

    PaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }
}
