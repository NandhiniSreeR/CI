package com.tw.bootcamp.bookshop.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {
    private Boolean status;
}
