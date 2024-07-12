package com.example.football.nice.apis.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Team {

    @Id
    @SequenceGenerator(
            name = "team_id_seq",
            sequenceName = "team_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_id_seq"
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String acronym;
    @Column(nullable = false)
    private Double budget;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    // Default constructor
    public Team() {
        this.players = new ArrayList<>();
    }

    // Constructor with parameters
    public Team(String name, String acronym, Double budget) {
        this.name = name;
        this.acronym = acronym;
        this.budget = budget;

    }
}
