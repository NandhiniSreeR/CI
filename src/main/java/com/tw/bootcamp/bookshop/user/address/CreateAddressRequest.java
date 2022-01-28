package com.tw.bootcamp.bookshop.user.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateAddressRequest {
    @Schema(example = "Jane Doe", description = "Name of the customer")
    private String fullName;
    @Schema(example = "9876543210", description = "Mobile number of the customer")
    private Long mobileNumber;
    @Schema(example = "24/l XYZ society", description = "Address line 1")
    @NotBlank
    private String lineNoOne;
    @Schema(example = "Near some hotel", description = "Address line 2")
    private String lineNoTwo;
    @NotBlank
    @Schema(example = "Pune", description = "City Name")
    private String city;
    @Schema(example = "Maharashtra", description = "State Name")
    private String state;
    @NotBlank
    @Schema(example = "411006", description = "Pin code")
    private String pinCode;
    @NotBlank
    @Schema(example = "India", description = "Country Name")
    private String country;
    @Schema(example = "true", description = "User's default shipping address")
    private boolean isDefault;

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
