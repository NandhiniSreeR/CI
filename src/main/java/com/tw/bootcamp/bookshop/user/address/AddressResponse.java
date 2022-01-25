package com.tw.bootcamp.bookshop.user.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    @Schema(example = "1", description = "Unique Identifier of Address")
    private Long id;
    @NotBlank
    @Schema(example = "24/l XYZ society", description = "Address line 1")
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
}
