package com.example.football.nice.apis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Player.
 */
@Getter
@Setter

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("position")
    private String position;
    @JsonProperty("jerseyNumber")
    private Integer jerseyNumber;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("nationality")
    private String nationality;
    @JsonProperty("size")
    private String size;// s, m, xl, etc.
    @JsonProperty("salary")
    private Double salary;
    @JsonProperty("teamId")
    private Long teamId;    // ID for modification
    @JsonProperty("teamName")
    private String teamName; // Name for retrieval
}
