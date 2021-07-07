package com.bikeescape.api.model;

import com.bikeescape.api.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Race")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Race {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "Id")
    @Size(max = 32)
    private String id;

    @Column(name = "Name")
    @NotNull
    private String name;

    @Column(name = "Description", length = 2000)
    @NotNull
    private String description;

    @Column(name = "Summary", length = 2000)
    @NotNull
    private String summary;

    @Column(name = "FailDescription", length = 2000)
    @NotNull
    private String failDescription;

    @Column(name = "Checkpoints")
    private Long checkpoints;

    @Column(name = "EstimatedTime")
    private Long estimatedTime;

    @Column(name = "City")
    private String city;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "IsPublic")
    private boolean isPublic;

    @Column(name = "Created")
    private Date created;

    @ManyToOne
    @JoinColumn(name = "AuthorId")
    @JsonManagedReference
    private User user;

    //reference to Difficulty table
    @ManyToOne
    @JoinColumn(name = "DifficultyId")
    private Difficulty difficulty;

    //reference to RaceType table
    @ManyToOne
    @JoinColumn(name = "RaceTypeId")
    @JsonManagedReference
    private RaceType raceType;

    // reference to Race Checkpoint table
    @OneToMany(mappedBy = "race")
    @JsonIgnore
    @JsonBackReference
    private Set<RaceCheckpoint> raceCheckpoints = new HashSet<>(0);

    @Override
    public String toString() {
        return "Race{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", summary='" + summary + '\'' +
                ", failDescription='" + failDescription + '\'' +
                ", estimatedTime=" + estimatedTime +
                ", city='" + city + '\'' +
                ", isActive=" + isActive +
                ", isPublic=" + isPublic +
                ", created=" + created +
                ", authorId=" + (user != null ? user.getId() : "No user!") +
                ", difficulty=" + (difficulty != null ? difficulty.getName() : "No difficulty!")+
                ", raceType=" + (raceType != null ? raceType.getName() : "No race type!")+
                '}';
    }

}
