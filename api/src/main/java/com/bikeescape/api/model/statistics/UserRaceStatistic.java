package com.bikeescape.api.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRaceStatistic {

    private String email;
    private String firstName;
    private long count;

}
