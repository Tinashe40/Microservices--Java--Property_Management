package com.proveritus.cloudutility.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private String street;
    private String city;
    private String state;
    private String zip;
}