package com.tw.bootcamp.bookshop.payment;

import com.tw.bootcamp.bookshop.payment.exception.InvalidCreditCardDetailsException;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Matcher;

@Getter
@Builder
public class CreditCardDetailsRequest {

    public static final String VALID_CVV_REGEX = "^[0-9]{3,4}$";
    public static final String VALID_EXPIRY_REGEX = "(?:0[1-9]|1[0-2])/[0-9]{4}";
    public static final String EXPIRY_DATE_FORMAT = "MM/yyyy";

    private CreditCardDetailsRequest(Long cardNumber, String cvv, String expiresOn, String cardHolderName) {
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
    @Pattern(regexp = VALID_EXPIRY_REGEX, message = "Expiry date format should be MM/YYYY")
    private String expiresOn;

    private String cardHolderName;

    public static void validate(CreditCardDetailsRequest request) throws ParseException {
        create(request.getCardNumber(), request.getCvv(), request.getExpiresOn(), request.getCardHolderName());
    }

    public static CreditCardDetailsRequest create(Long cardNumber, String cvv, String expiresOn, String cardHolderName) throws ParseException {
        Date expiryDate = formatAsDate(expiresOn);
        System.out.println("expiryDate = " + expiryDate);
        if (cardNumber < 1000_0000_0000_0000L || cardNumber > 9999_9999_9999_9999L) throw new InvalidCreditCardDetailsException("Invalid Credit Card Number");
        if (!isValidCVVNumber(cvv)) throw new InvalidCreditCardDetailsException("Invalid CVV");
        if (expiryDate.before(Date.from(Instant.now()))) throw new InvalidCreditCardDetailsException("Invalid Expiry Date");

        return new CreditCardDetailsRequest(cardNumber, cvv, expiresOn, cardHolderName);
    }

    private static Date formatAsDate(String expiresOn) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXPIRY_DATE_FORMAT);
        dateFormat.setLenient(false);
        return dateFormat.parse(expiresOn);
    }

    public static boolean isValidCVVNumber(String str)
    {
        // Regex to check valid CVV number.
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(VALID_CVV_REGEX);
        // If the string is empty return false
        if (str == null)
        {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
