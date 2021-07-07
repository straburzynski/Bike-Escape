package com.bikeescape.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceHeader {

    private String id;
    private String name;
    private String description;
    private String city;
    private String raceTypeName;
    private String estimatedTime;
    private String difficultyName;
    private String created;

}
