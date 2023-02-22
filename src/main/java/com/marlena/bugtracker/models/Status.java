package com.marlena.bugtracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {

    TODO, IN_PROGRESS, DONE, BLOCKED;

    @JsonCreator
    public static Status fromString(@JsonProperty("status") String value) {
        return Status.valueOf(value);
    }

}
