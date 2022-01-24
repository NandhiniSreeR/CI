package com.tw.bootcamp.bookshop.money;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MoneyTest {

    @Test
    void shouldGetINRCurrencyWhenRupeesIsSet() {
        Money money = new Money();
        money = Money.rupees(20);
        assertEquals("INR", money.getCurrency());
        assertEquals(Money.rupees(76), new Money(Country.INDIA.currency, 76));
    }

}
