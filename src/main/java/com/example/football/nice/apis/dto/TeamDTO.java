package com.example.football.nice.apis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO {
    private Long id;

    @NotEmpty(message = "Team name cannot be empty")
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "Acronym cannot be empty")
    @JsonProperty("acronym")
    private String acronym;

    @NotNull(message = "Budget cannot be null")
    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("players")
    private List<PlayerDTO> players;
}
