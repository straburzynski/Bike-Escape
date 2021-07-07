package com.bikeescape.api.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationTO {

    private String email;
    private String firstName;
    private String password;

    @Override
    public String toString() {
        return "UserRegistrationTO{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
