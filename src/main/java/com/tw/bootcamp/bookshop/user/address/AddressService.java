package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address create(@Valid CreateAddressRequest createRequest, User user) {
        Address address = Address.create(createRequest, user);
        return addressRepository.save(address);
    }

    public List<Address> loadAddressForUser(User user) {
        return addressRepository.findAllByUser(user);
    }

    public Optional<Address> loadAddressById(Long addressId){
        return addressRepository.findById(addressId);
    }
}
