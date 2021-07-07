package com.bikeescape.api.model;


import com.bikeescape.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private int id;

    //reference to User table
    @ManyToOne
    @JoinColumn(name = "UserId")
    @JsonManagedReference
    @NotNull
    private User user;
}
