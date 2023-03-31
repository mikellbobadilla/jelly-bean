package com.mikellbobadilla.jellyBean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRecord(
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String username,
        String email,
        String password
) {
}
