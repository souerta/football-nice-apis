package com.example.football.nice.apis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String name;

    @NotEmpty(message = "Acronym cannot be empty")
    private String acronym;

    @NotNull(message = "Budget cannot be null")
    private Double budget;

    private List<PlayerDTO> players;
}
