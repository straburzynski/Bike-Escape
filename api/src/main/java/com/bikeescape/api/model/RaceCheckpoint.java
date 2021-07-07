package com.bikeescape.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "RaceCheckpoint")
@JsonIgnoreProperties(value = { "race" })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceCheckpoint {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "Id")
    @Size(max = 32)
    private String id;

    //reference to Race table
    @ManyToOne
    @JoinColumn(name = "RaceId")
    @JsonManagedReference
    @NotNull
    private Race race;

    @NotNull
    @Column(name = "Number")
    private Long number;

    @NotNull
    @Column(name = "Name")
    private String name;

    @NotNull
    @Column(name = "Description", length = 1000)
    private String description;

    @NotNull
    @Column(name = "Question")
    private String question;

    @NotNull
    @Column(name="AnswerType")
    @Enumerated(value = EnumType.STRING)
    private AnswerType answerType = AnswerType.TEXT;

    @NotNull
    @Column(name = "Answer")
    private String answer;

    @NotNull
    @Column(name = "Hint")
    private String hint;

    @NotNull
    @Column(name = "Latitude")
    private String latitude;

    @NotNull
    @Column(name = "Longitude")
    private String longitude;

    @Override
    public String toString() {
        return "RaceCheckpoint{" +
                "id='" + id + '\'' +
                ", race=" + (race != null ? race.getId() : "No race!") +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", question='" + question + '\'' +
                ", answerType=" + answerType.getType() +
                ", answer='" + answer + '\'' +
                ", hint='" + hint + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
