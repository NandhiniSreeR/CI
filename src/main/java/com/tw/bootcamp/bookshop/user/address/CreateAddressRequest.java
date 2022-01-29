package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.error.AddressErrorResponse;
import com.tw.bootcamp.bookshop.error.AddressNotValidException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.regex.Pattern;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateAddressRequest {

    private static final String INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS = "Invalid Characters in this Field.Accepts only Alphabets";
    private static final String INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS_AND_NUMBERS
            = "Invalid Characters in this Field.Accepts only Alphabets and Numbers";
    private static final String INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_NUMBERS = "Invalid Mobile Number.Accepts only 10 digit Numbers";


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

    public void validate() {
        AddressErrorResponse addressErrorResponse = new AddressErrorResponse("Failed to save address as some of the fields are invalid");
        if (getCity() != null && !Pattern.matches(AddressService.ADDRESS_CITY_COUNTRY_STATE_REGEX, getCity())) {
            addressErrorResponse.addError("city", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS);
        }
        if (getCountry() != null && !Pattern.matches(AddressService.ADDRESS_CITY_COUNTRY_STATE_REGEX, getCountry())) {
            addressErrorResponse.addError("country", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS);
        }
        if (getState() != null && !Pattern.matches(AddressService.ADDRESS_CITY_COUNTRY_STATE_REGEX, getState())) {
            addressErrorResponse.addError("state", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS);
        }
        if (getPinCode() != null && !Pattern.matches(AddressService.ADDRESS_PINCODE_REGEX, getPinCode())) {
            addressErrorResponse.addError("pincode", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS_AND_NUMBERS);
        }
        if (getMobileNumber() != null) {
            String mobileNumber = String.valueOf(getMobileNumber());
            if (mobileNumber.length() > 10 || !Pattern.matches(AddressService.ADDRESS_MOBILE_NUMBER_REGEX, mobileNumber)) {
                addressErrorResponse.addError("mobileNumber", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_NUMBERS);
            }
        }
        if (addressErrorResponse.hasAnyErrors()) {
            throw new AddressNotValidException(addressErrorResponse);
        }
    }
}
