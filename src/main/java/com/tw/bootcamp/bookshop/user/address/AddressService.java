package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.error.AddressErrorResponse;
import com.tw.bootcamp.bookshop.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;


    public static final String ADDRESS_CITY_REGEX = "[0-9]+";

    public Address create(@Valid CreateAddressRequest createRequest, User user) {
        Address address = Address.create(createRequest, user);
        return addressRepository.save(address);
    }

    public List<Address> loadAddressFromUserName(User user) {
        return addressRepository.findAllByUser(user);
    }

    public Optional<Address> loadAddressById(Long addressId){
        return addressRepository.findById(addressId);
    }

    public AddressErrorResponse validate(CreateAddressRequest createAddressRequest) {
        AddressErrorResponse addressErrorResponse = new AddressErrorResponse("Some Fields are Invalid");
        if(Pattern.matches(ADDRESS_CITY_REGEX,createAddressRequest.getCity()))
        {
            addressErrorResponse.addError("city","Invalid Characters in this Field.Accepts only Alphabets");
        }

        return addressErrorResponse;
    }
}