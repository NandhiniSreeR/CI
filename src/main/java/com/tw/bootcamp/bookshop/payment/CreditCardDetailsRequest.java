package com.tw.bootcamp.bookshop.payment;

import com.tw.bootcamp.bookshop.payment.exception.InvalidCreditCardDetailsException;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Matcher;

@Getter
@Builder
public class CreditCardDetailsRequest {

    public static final String VALID_CVV_REGEX = "^[0-9]{3,4}$";

    private CreditCardDetailsRequest(Long cardNumber, String cvv, Date expiresOn, String cardHolderName) {
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
    @Pattern(regexp = VALID_CVV_REGEX, message = "CVV should be 3 or 4 digits")
    private String cvv;

    @NotBlank
    @Future
    private Date expiresOn;

    private String cardHolderName;

    public static CreditCardDetailsRequest validate(CreditCardDetailsRequest request) {
        return create(request.getCardNumber(), request.getCvv(), request.getExpiresOn(), request.getCardHolderName());
    }

    public static CreditCardDetailsRequest create(Long cardNumber, String cvv, Date expiresOn, String cardHolderName) {
        if (cardNumber < 1000_0000_0000_0000L || cardNumber > 9999_9999_9999_9999L) throw new InvalidCreditCardDetailsException();
        if (isValidCVVNumber(cvv)) throw new InvalidCreditCardDetailsException();
        if (expiresOn.before(Date.from(Instant.now()))) throw new InvalidCreditCardDetailsException();

        return new CreditCardDetailsRequest(cardNumber, cvv, expiresOn, cardHolderName);
    }

    public static boolean isValidCVVNumber(String str)
    {
        // Regex to check valid CVV number.
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(VALID_CVV_REGEX);
        // If the string is empty
        // return false
        if (str == null)
        {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
