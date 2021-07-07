package com.bikeescape.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnswerType {
    TEXT("text"), QR("qr");

    private String type;

    AnswerType(String type) {
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
