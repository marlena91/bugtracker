package com.marlena.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Priority {

    MINOR, NORMAL, MAJOR, CRITICAL;

    @JsonCreator
    public static Priority fromString(@JsonProperty("priority") String value) {
        return Priority.valueOf(value);
    }

}
