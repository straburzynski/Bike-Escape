package com.bikeescape.api.model.userrace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRaceTO {

    private Long userId;
    private String raceId;
    private String raceStatus;
    private int totalTime;

}
