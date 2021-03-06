package com.tw.bootcamp.bookshop.user.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tw.bootcamp.bookshop.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Schema(example = "true", description = "User's default shipping address")
    private boolean isDefault;
    @Schema(example = "Jane Doe", description = "Name of the customer")
   // @Column(name = "full_name")
    private String fullName;
    @Schema(example = "9876543210", description = "Mobile number of the customer")
   // @Column(name = "mobile_number")
    private Long mobileNumber;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address() {
        this.isDefault = false;
    }

    public Address(String lineNoOne, String lineNoTwo, String city, String state, String pinCode, String country, User user,String fullname,Long mobileNumber,boolean isDefault) {
        this.lineNoOne = lineNoOne;
        this.lineNoTwo = lineNoTwo;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
        this.user = user;
        this.fullName = fullname;
        this.mobileNumber = mobileNumber;
        this.isDefault = isDefault;
    }

    public static Address create(CreateAddressRequest createRequest, User user) {
        return new Address(createRequest.getLineNoOne(),
                createRequest.getLineNoTwo(),
                createRequest.getCity(),
                createRequest.getState(),
                createRequest.getPinCode(),
                createRequest.getCountry(),
                user,
                createRequest.getFullName(),
                createRequest.getMobileNumber(),
                createRequest.isDefault()
        );
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
                .fullName(fullName)
                .mobileNumber(mobileNumber)
                .defaultShippingAddress(isDefault)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Address address = (Address) o;
        return id != null && Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
