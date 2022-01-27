package com.tw.bootcamp.bookshop.payment;

import com.tw.bootcamp.bookshop.payment.exception.InvalidCreditCardDetailsException;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Date;

@Getter
@Builder
public class CreditCardDetailsRequest {

    private CreditCardDetailsRequest(Long cardNumber, int cvv, Date expiresOn, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiresOn = expiresOn;
        this.cardHolderName = cardHolderName;
    }

    @NotBlank
    @Min(value = 1000_0000_0000_0000L, message = "Card number should be 16 digits")
    @Max(value = 9999_9999_9999_9999L, message = "Card number should be 16 digits")
    private Long cardNumber;

    @NotBlank
    @Min(value = 100, message = "CVV should be 3 digits")
    @Max(value = 999, message = "CVV should be 3 digits")
    private int cvv;

    @NotBlank
    @Future
    private Date expiresOn;

    private String cardHolderName;

    public static CreditCardDetailsRequest validate(CreditCardDetailsRequest request) {
        return create(request.getCardNumber(), request.getCvv(), request.getExpiresOn(), request.getCardHolderName());
    }

    public static CreditCardDetailsRequest create(Long cardNumber, int cvv, Date expiresOn, String cardHolderName) {
        if (cardNumber < 1000_0000_0000_0000L || cardNumber > 9999_9999_9999_9999L) throw new InvalidCreditCardDetailsException();
        if (cvv < 100 || cvv > 999) throw new InvalidCreditCardDetailsException();
        if (expiresOn.before(Date.from(Instant.now()))) throw new InvalidCreditCardDetailsException();

        return new CreditCardDetailsRequest(cardNumber, cvv, expiresOn, cardHolderName);
    }

}
