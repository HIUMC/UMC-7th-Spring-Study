package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class Address {

    private String street;
    private String city;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }
}
