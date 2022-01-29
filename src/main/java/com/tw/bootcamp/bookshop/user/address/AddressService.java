package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.error.AddressErrorResponse;
import com.tw.bootcamp.bookshop.error.AddressNotValidException;
import com.tw.bootcamp.bookshop.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AddressService {


    @Autowired
    private AddressRepository addressRepository;


    public static final String ADDRESS_CITY_COUNTRY_STATE_REGEX = "[A-Za-z]+";
    public static final String ADDRESS_PINCODE_REGEX = "^[A-Za-z0-9]*";
    public static final String ADDRESS_MOBILE_NUMBER_REGEX = "[\\d]{10}";


    public Address create(@Valid CreateAddressRequest createRequest, User user) {
        createRequest.validate();
        if(Objects.nonNull(user)) {
            List<Address> addresses = addressRepository.findAddressesByUserId(user.getId());
            if (addresses.isEmpty()) {
                createRequest.setDefault(true);
            }
        }
        Address address = Address.create(createRequest, user);
        return addressRepository.save(address);
    }

    public List<Address> loadAddressForUser(User user) {
        return addressRepository.findAllByUser(user);
    }

    public Optional<Address> loadAddressById(Long addressId) {
        return addressRepository.findById(addressId);
    }

}