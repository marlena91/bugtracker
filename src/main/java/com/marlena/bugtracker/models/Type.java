package com.marlena.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Type {

    TASK, BUG, PERFORMANCE_ISSUE, FEATURE;

    @JsonCreator
    public static Type fromString(@JsonProperty("type") String value) {
        return Type.valueOf(value);
    }

}
