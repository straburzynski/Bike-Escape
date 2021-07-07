package com.bikeescape.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RaceStatus {
    IN_PROGRESS("in_progress"),
    FINISHED("finished"),
    FAILED("failed");

    private String type;

    RaceStatus(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
