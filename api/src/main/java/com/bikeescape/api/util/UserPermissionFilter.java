package com.bikeescape.api.util;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.user.User;
import com.bikeescape.api.security.AuthService;
import com.bikeescape.api.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserPermissionFilter {

    private final AuthService authService;
    private final RaceService raceService;

    @Autowired
    public UserPermissionFilter(AuthService authService, RaceService raceService) {
        this.authService = authService;
        this.raceService = raceService;
    }

    public Long getUserIdFromRequest(HttpServletRequest request) {
        User user = authService.getLoggedUser(request);
        return user.getId();
    }

    public boolean canUserChangeRace(String raceId, HttpServletRequest request) {
        Race race = raceService.findOne(raceId);
        if (race == null) throw new RaceValidationException("No race found!");
        User user = authService.getLoggedUser(request);
        return race.getUser().getId().equals(user.getId());
    }

    public boolean isUserAdmin(HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN");
    }

}