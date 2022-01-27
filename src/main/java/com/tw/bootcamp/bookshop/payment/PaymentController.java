package com.tw.bootcamp.bookshop.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping("/creditcards")
    @Operation(summary = "Make Credit Card Payment", description = "API to make credit card payment and validate if the credit card details are correct", tags = {"Payment Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Payment Successful", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = PaymentResponse.class))})}
    )
    public ResponseEntity<PaymentResponse> makeCreditCardPayment(@RequestBody CreditCardDetailsRequest ccRequest) {
        CreditCardDetailsRequest.validate(ccRequest);
        PaymentResponse response = PaymentResponse.builder().status(true).build();
        return ResponseEntity.ok(response);
    }
}
