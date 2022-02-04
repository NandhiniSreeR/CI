package com.tw.bootcamp.bookshop.user.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateOrderRequest {

    //private List<String> paymentModeList

    @NotNull
    @Schema(example = "1", description = "Order Quantity")
    private int quantity;
    @NotNull
    @Schema(example = "CREDIT_CARD", description = "Mode of Payment")
    @Value("CASH_ON_DELIVERY")
    private PaymentMode paymentMode;
    @NotNull
    @Schema(example = "1", description = "Unique Identifier of the Address")
    private Long addressId;
    @NotNull
    @Schema(example = "1", description = "Unique Identifier of the Book")
    private Long bookId;
}
