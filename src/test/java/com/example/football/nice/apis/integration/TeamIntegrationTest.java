package com.example.football.nice.apis.integration;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.dto.TeamDTO;
import com.example.football.nice.apis.repository.PlayerRepository;
import com.example.football.nice.apis.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TeamIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
    }

    @Test
    void testCreateTeam() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);

        ResponseEntity<TeamDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Team Name");
    }

    @Test
    void testGetTeamById() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        ResponseEntity<TeamDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/teams/" + teamResponse.getBody().getId(), TeamDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Team Name");
    }

    @Test
    void testUpdateTeam() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        // Update the team
        TeamDTO updatedTeamDTO = new TeamDTO();
        updatedTeamDTO.setName("Updated Team Name");
        updatedTeamDTO.setAcronym("UTN");
        updatedTeamDTO.setBudget(200000.0);
        HttpEntity<TeamDTO> requestUpdate = new HttpEntity<>(updatedTeamDTO);

        ResponseEntity<TeamDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/teams/" + teamResponse.getBody().getId(), HttpMethod.PUT, requestUpdate, TeamDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Team Name");
    }

    @Test
    void testCreateTeamWithPlayers() {
        // Create a team with players
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);

        PlayerDTO player1 = new PlayerDTO();
        player1.setFirstName("John");
        player1.setLastName("Doe");
        player1.setPosition("Forward");

        PlayerDTO player2 = new PlayerDTO();
        player2.setFirstName("Jane");
        player2.setLastName("Doe");
        player2.setPosition("Midfielder");

        teamDTO.setPlayers(List.of(player1, player2));

        ResponseEntity<TeamDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPlayers()).hasSize(2);
    }

    @Test
    void testUpdateTeamWithPlayers() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);

        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        // Update the team with players
        TeamDTO updatedTeamDTO = new TeamDTO();
        updatedTeamDTO.setName("Updated Team Name");
        updatedTeamDTO.setAcronym("UTN");
        updatedTeamDTO.setBudget(200000.0);

        PlayerDTO player1 = new PlayerDTO();
        player1.setFirstName("John");
        player1.setLastName("Doe");
        player1.setPosition("Forward");

        PlayerDTO player2 = new PlayerDTO();
        player2.setFirstName("Jane");
        player2.setLastName("Doe");
        player2.setPosition("Midfielder");

        updatedTeamDTO.setPlayers(List.of(player1, player2));

        HttpEntity<TeamDTO> requestUpdate = new HttpEntity<>(updatedTeamDTO);
        ResponseEntity<TeamDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/teams/" + teamResponse.getBody().getId(), HttpMethod.PUT, requestUpdate, TeamDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPlayers()).hasSize(2);
    }

    @Test
    void testCreateTeamWithDuplicatePlayers() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);

        List<PlayerDTO> players = new ArrayList<>();
        PlayerDTO player1 = new PlayerDTO();
        player1.setFirstName("John");
        player1.setLastName("Doe");
        player1.setPosition("Forward");

        PlayerDTO player2 = new PlayerDTO();
        player2.setFirstName("John");
        player2.setLastName("Doe");
        player2.setPosition("Midfielder");

        players.add(player1);
        players.add(player2);

        teamDTO.setPlayers(players);

        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).contains("Duplicate player detected: John Doe");
    }

    @Test
    void testUpdateTeamWithDuplicatePlayers() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        // Update the team with duplicate players
        teamDTO.setPlayers(new ArrayList<>());
        PlayerDTO player1 = new PlayerDTO();
        player1.setFirstName("John");
        player1.setLastName("Doe");
        player1.setPosition("Forward");

        PlayerDTO player2 = new PlayerDTO();
        player2.setFirstName("John");
        player2.setLastName("Doe");
        player2.setPosition("Midfielder");

        teamDTO.getPlayers().add(player1);
        teamDTO.getPlayers().add(player2);

        HttpEntity<TeamDTO> requestUpdate = new HttpEntity<>(teamDTO);
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/teams/" + teamResponse.getBody().getId(), HttpMethod.PUT, requestUpdate, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).contains("Duplicate player detected: John Doe");
    }

    @Test
    void testDeleteTeam() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        // Delete the team
        restTemplate.withBasicAuth("admin", "admin123")
                .delete("/api/teams/" + teamResponse.getBody().getId());

        // Try to get the deleted team
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/teams/" + teamResponse.getBody().getId(), String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void getAllTeams_withBasicAuth_shouldReturnOk() {
        ResponseEntity<TeamDTO[]> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/teams", TeamDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllTeams_withoutAuth_shouldReturnUnauthorized() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/teams", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getAllTeams_withPaginationAndSorting_shouldReturnOk() {
        // Create some teams to test pagination and sorting
        for (int i = 1; i <= 20; i++) {
            TeamDTO teamDTO = new TeamDTO();
            teamDTO.setName("Team " + i);
            teamDTO.setAcronym("T" + i);
            teamDTO.setBudget(100000.0 + i);
            restTemplate.withBasicAuth("admin", "admin123")
                    .postForEntity("/api/teams", teamDTO, TeamDTO.class);
        }

        // Get first page with size 10, sorted by name
        ResponseEntity<TeamDTO[]> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/teams?page=0&size=10&sortBy=name", TeamDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(10);

        // Verify sorting by name
        List<TeamDTO> teams = List.of(response.getBody());
        for (int i = 1; i < teams.size(); i++) {
            assertThat(teams.get(i).getName()).isGreaterThanOrEqualTo(teams.get(i - 1).getName());
        }
    }
}
