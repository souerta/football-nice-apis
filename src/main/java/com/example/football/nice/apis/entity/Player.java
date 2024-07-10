package com.example.football.nice.apis.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "player", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"firstName", "lastName", "position", "jerseyNumber", "age", "nationality", "size", "salary"})
})
public class Player {

    @Id
    @SequenceGenerator(
            name = "player_id_seq",
            sequenceName = "player_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_id_seq"
    )
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String position;
    private Integer jerseyNumber;
    private Integer age;
    private String nationality;
    private String size;
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(firstName, player.firstName) &&
                Objects.equals(lastName, player.lastName) &&
                Objects.equals(position, player.position) &&
                Objects.equals(jerseyNumber, player.jerseyNumber) &&
                Objects.equals(age, player.age) &&
                Objects.equals(nationality, player.nationality) &&
                Objects.equals(size, player.size) &&
                Objects.equals(salary, player.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, position, jerseyNumber, age, nationality, size, salary);
    }
}
