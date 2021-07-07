package com.bikeescape.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RaceType")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private int id;

    @Column(name = "Name", unique = true, nullable = false)
    private String name;

    // reference to Race table
    @OneToMany(mappedBy = "raceType")
    @JsonBackReference
    private Set<Race> races = new HashSet<>(0);

}
