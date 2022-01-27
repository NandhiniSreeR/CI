package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;

public class AddressTestBuilder {
    private final Address.AddressBuilder addressBuilder;

    public AddressTestBuilder() {
        this.addressBuilder = Address.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22 001")
                .country("Surrey");
    }

    public AddressTestBuilder withUser(User user) {
        addressBuilder.user(user);
        return this;
    }

    public Address build() {
        return addressBuilder.build();
    }
}
