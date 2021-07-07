package com.bikeescape.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "UserToken")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {

    @Id
    @Column(name = "UserId", unique = true)
    private Long userId;

    @Column(name = "Token", length = 32)
    private String token;

}
