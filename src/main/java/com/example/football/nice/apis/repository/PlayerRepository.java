package com.example.football.nice.apis.repository;

import com.example.football.nice.apis.entity.Player;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    @Modifying
    @Transactional
    @Query("DELETE FROM Player p WHERE p.team.id = :teamId")
    void deleteByTeamId(Long teamId);

     boolean existsByFirstNameAndLastNameAndPositionAndJerseyNumberAndAgeAndNationalityAndSizeAndSalary(
            String firstName, String lastName, String position, int jerseyNumber,
            int age, String nationality, String size, double salary);

    List<Player> findByTeamId(Long id);

    Player findByFirstNameAndLastNameAndPositionAndJerseyNumberAndAgeAndNationalityAndSizeAndSalary(String firstName, String lastName, String position, Integer jerseyNumber, Integer age, String nationality, String size, Double salary);
}
