package com.bikeescape.api.model.userrace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRaceRankingTO {

    private Long userId;
    private String userName;
    private String raceId;
    private String raceName;
    private String city;
    private String raceStatus;
    private int totalTime;
    private String finishDate;

}
