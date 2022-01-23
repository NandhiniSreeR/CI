package com.tw.bootcamp.bookshop.money;

import com.opencsv.bean.CsvBindByName;
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
    @CsvBindByName(column = "currency")
    private String currency;
    @CsvBindByName(column = "amount")
    @Column(columnDefinition = "NUMERIC")
    private double amount;

    public static Money rupees(double amount) {
        return new Money(Country.INDIA.currency, amount);
    }
}
