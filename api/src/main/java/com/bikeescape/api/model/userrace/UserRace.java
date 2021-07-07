package com.bikeescape.api.model.userrace;

import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.RaceStatus;
import com.bikeescape.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "UserRace")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private Long id;

    //reference to Race table
    @ManyToOne
    @JoinColumn(name = "RaceId")
    @JsonManagedReference
    @NotNull
    private Race race;

    //reference to User table
    @ManyToOne
    @JoinColumn(name = "UserId")
    @JsonManagedReference
    @NotNull
    private User user;

    @NotNull
    @Column(name="RaceStatus")
    @Enumerated(value = EnumType.STRING)
    private RaceStatus raceStatus;

    @Column(name = "TotalTime")
    private int totalTime;

    @Column(name = "FinishDate")
    private Date finishDate;

    @Override
    public String toString() {
        return "UserRace{" +
                "id=" + id +
                ", race=" + race.getId() +
                ", user=" + user.getEmail() +
                ", raceStatus=" + raceStatus.getType() +
                ", totalTime=" + totalTime +
                ", finishDate=" + finishDate +
                '}';
    }
}