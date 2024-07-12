package com.example.football.nice.apis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * Data Transfer Object for Team.
 * This class is used to transfer team data between different layers of the application.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO {
    /**
     * The unique identifier of the team.
     */
    private Long id;
    /**
     * The name of the team.
     * Cannot be empty.
     */
    @NotEmpty(message = "Team name cannot be empty")
    @JsonProperty("name")
    private String name;
    /**
     * The acronym of the team.
     * Cannot be empty.
     */
    @NotEmpty(message = "Acronym cannot be empty")
    @JsonProperty("acronym")
    private String acronym;
    /**
     * The budget of the team.
     * Cannot be null.
     */
    @NotNull(message = "Budget cannot be null")
    @JsonProperty("budget")
    private Double budget;
    /**
     * The list of players in the team.
     */
    @JsonProperty("players")
    private List<PlayerDTO> players;
}
