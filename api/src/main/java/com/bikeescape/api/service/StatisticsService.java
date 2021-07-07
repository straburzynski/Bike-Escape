package com.bikeescape.api.service;

import com.bikeescape.api.model.statistics.Statistics;
import org.springframework.security.access.prepost.PreAuthorize;

public interface StatisticsService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Statistics getStatistics();
}
