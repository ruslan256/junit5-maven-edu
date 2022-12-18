package com.ruslan.edu.junit5.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class User {

    Integer id;
    String username;
    String password;

}
