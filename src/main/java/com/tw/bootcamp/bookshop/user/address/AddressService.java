package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.error.AddressErrorResponse;
import com.tw.bootcamp.bookshop.error.AddressNotValidException;
import com.tw.bootcamp.bookshop.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AddressService {
    public static final String INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS = "Invalid Characters in this Field.Accepts only Alphabets";
    private static final String INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS_AND_NUMBERS
            = "Invalid Characters in this Field.Accepts only Alphabets and Numbers";
    public static final String INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_NUMBERS = "Invalid Mobile Number.Accepts only 10 digit Numbers";

    @Autowired
    private AddressRepository addressRepository;


    public static final String ADDRESS_CITY_COUNTRY_STATE_REGEX = "[0-9]+";
    public static final String ADDRESS_PINCODE_REGEX = "^[A-Za-z0-9]*";
    public static final String ADDRESS_MOBILE_NUMBER_REGEX = "[\\d]{10}";


    public Address create(@Valid CreateAddressRequest createRequest, User user) {
        AddressErrorResponse validationResponse = validate(createRequest);
        if (validationResponse.hasAnyErrors()) {
            throw new AddressNotValidException(validationResponse);
        }
        Address address = Address.create(createRequest, user);
        return addressRepository.save(address);
    }

    public List<Address> loadAddressFromUserName(User user) {
        return addressRepository.findAllByUser(user);
    }

    public Optional<Address> loadAddressById(Long addressId) {
        return addressRepository.findById(addressId);
    }

    public AddressErrorResponse validate(CreateAddressRequest createAddressRequest) {
        AddressErrorResponse addressErrorResponse = new AddressErrorResponse("Failed to save address as some of the fields are invalid");
        if (createAddressRequest.getCity() != null && Pattern.matches(ADDRESS_CITY_COUNTRY_STATE_REGEX, createAddressRequest.getCity())) {
            addressErrorResponse.addError("city", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS);
        }
        if (createAddressRequest.getCountry() != null && Pattern.matches(ADDRESS_CITY_COUNTRY_STATE_REGEX, createAddressRequest.getCountry())) {
            addressErrorResponse.addError("country", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS);
        }
        if (createAddressRequest.getState() != null && Pattern.matches(ADDRESS_CITY_COUNTRY_STATE_REGEX, createAddressRequest.getState())) {
            addressErrorResponse.addError("state", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS);
        }
        if (createAddressRequest.getPinCode() != null && !Pattern.matches(ADDRESS_PINCODE_REGEX, createAddressRequest.getPinCode())) {
            addressErrorResponse.addError("pincode", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_ALPHABETS_AND_NUMBERS);
        }
        if (createAddressRequest.getMobileNumber() != null) {
            String mobileNumber = String.valueOf(createAddressRequest.getMobileNumber());
            if (mobileNumber.length() > 10 || !Pattern.matches(ADDRESS_MOBILE_NUMBER_REGEX, mobileNumber)) {
                addressErrorResponse.addError("mobileNumber", INVALID_CHARACTERS_IN_THIS_FIELD_ACCEPTS_ONLY_NUMBERS);
            }
        }
        return addressErrorResponse;
    }
}