package com.bikeescape.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Difficulty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Difficulty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private int id;

    @Column(name = "Name", unique = true, nullable = false)
    private String name;

}
