package com.bikeescape.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceDTO {

    private String id;
    private String name;
    private String description;
    private String summary;
    private String failDescription;
    private String raceImage;
    private String summaryImage;
    private String failImage;
    private Long checkpoints;
    private Long estimatedTime;
    private String city;
    private Boolean isActive;
    private Boolean isPublic;
    private String difficulty;
    private String raceType;
    private Long authorId;


    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return "RaceDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", summary='" + summary + '\'' +
                ", failDescription='" + failDescription + '\'' +
                ", checkpoints=" + checkpoints +
                ", estimatedTime=" + estimatedTime +
                ", city='" + city + '\'' +
                ", isActive=" + isActive +
                ", isPublic=" + isPublic +
                ", difficulty='" + difficulty + '\'' +
                ", raceType='" + raceType + '\'' +
                '}';
    }
}
