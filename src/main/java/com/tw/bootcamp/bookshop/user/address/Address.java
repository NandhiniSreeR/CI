package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String lineNoOne;
    private String lineNoTwo;
    @NotBlank
    private String city;
    private String state;
    @NotBlank
    private String pinCode;
    @NotBlank
    private String country;
    private boolean isDefault;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address() {
        this.isDefault = false;
    }

    public Address(String lineNoOne, String lineNoTwo, String city, String state, String pinCode, String country, User user) {
        this.lineNoOne = lineNoOne;
        this.lineNoTwo = lineNoTwo;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
        this.user = user;
    }

    public static Address create(CreateAddressRequest createRequest, User user) {
        return new Address(createRequest.getLineNoOne(),
                createRequest.getLineNoTwo(),
                createRequest.getCity(),
                createRequest.getState(),
                createRequest.getPinCode(),
                createRequest.getCountry(),
                user);
    }

    public AddressResponse toResponse() {
        return AddressResponse.builder()
                .id(id)
                .lineNoOne(lineNoOne)
                .lineNoTwo(lineNoTwo)
                .city(city)
                .state(state)
                .country(country)
                .pinCode(pinCode)
                .build();
    }
}
