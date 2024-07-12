package com.example.football.nice.apis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Player.
 * This class is used to transfer player data between different layers of the application.
 */
@Getter
@Setter

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerDTO {
    /**
     * The unique identifier of the player.
     */
    @JsonProperty("id")
    private Long id;
    /**
     * The first name of the player.
     */
    @JsonProperty("firstName")
    private String firstName;
    /**
     * The last  name of the player.
     */
    @JsonProperty("lastName")
    private String lastName;
    /**
     * The position of the player in the team (e.g., forward, midfielder, defender, goalkeeper).
     */
    @JsonProperty("position")
    private String position;

    /**
     * The jersey number of the player.
     */
    @JsonProperty("jerseyNumber")
    private Integer jerseyNumber;
    /**
     * The age of the player.
     */
    @JsonProperty("age")
    private Integer age;
    /**
     * The nationality of the player.
     */
    @JsonProperty("nationality")
    private String nationality;
    /**
     * The size of the player's jersey (e.g., s, m, xl).
     */
    @JsonProperty("size")
    private String size;
    /**
     * The salary of the player.
     */
    @JsonProperty("salary")
    private Double salary;
    /**
     * The unique identifier of the team the player belongs to. Used for modification.
     */
    @JsonProperty("teamId")
    private Long teamId;    // ID for modification
    /**
     * The name of the team the player belongs to. Used for retrieval.
     */
    @JsonProperty("teamName")
    private String teamName; // Name for retrieval
}
