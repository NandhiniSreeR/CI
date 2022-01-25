package com.tw.bootcamp.bookshop.money;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Money {
    @Schema(example = "INR" , description = "Currency of a Country")
    private String currency;
    @Column(columnDefinition = "NUMERIC")
    @Schema(example = "1000" , description = "Price")
    private double amount;

    public static Money rupees(double amount) {
        return new Money("INR", amount);
    }
}
