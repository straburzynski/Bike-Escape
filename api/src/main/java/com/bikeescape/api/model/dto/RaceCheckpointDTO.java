package com.bikeescape.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceCheckpointDTO {

    private String id;
    private String raceId;
    private Long number;
    private String name;
    private String description;
    private String question;
    private String answerType;
    private String answer;
    private String hint;
    private String latitude;
    private String longitude;
    private String checkpointImage;

    @Override
    public String toString() {
        return "RaceCheckpointDTO{" +
                "id='" + id + '\'' +
                ", raceId='" + raceId + '\'' +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", question='" + question + '\'' +
                ", answerType='" + answerType + '\'' +
                ", answer='" + answer + '\'' +
                ", hint='" + hint + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}