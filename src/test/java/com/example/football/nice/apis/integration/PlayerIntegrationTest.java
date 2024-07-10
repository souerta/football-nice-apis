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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PlayerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void testCreatePlayer() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        // Create a player associated with the team
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("John");
        playerDTO.setLastName("Doe");
        playerDTO.setPosition("Forward");
        playerDTO.setTeamId(teamResponse.getBody().getId());

        ResponseEntity<PlayerDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/players", playerDTO, PlayerDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
    }

    @Test
    void testGetPlayerById() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        // Create a player associated with the team
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("John");
        playerDTO.setLastName("Doe");
        playerDTO.setPosition("Forward");
        playerDTO.setTeamId(teamResponse.getBody().getId());
        ResponseEntity<PlayerDTO> playerResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/players", playerDTO, PlayerDTO.class);

        ResponseEntity<PlayerDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/players/" + playerResponse.getBody().getId(), PlayerDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
    }

    @Test
    void testUpdatePlayer() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        // Create a player associated with the team
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("John");
        playerDTO.setLastName("Doe");
        playerDTO.setPosition("Forward");
        playerDTO.setTeamId(teamResponse.getBody().getId());
        ResponseEntity<PlayerDTO> playerResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/players", playerDTO, PlayerDTO.class);

        // Update the player
        PlayerDTO updatedPlayerDTO = new PlayerDTO();
        updatedPlayerDTO.setFirstName("Jane");
        updatedPlayerDTO.setLastName("Doe");
        updatedPlayerDTO.setPosition("Midfielder");
        HttpEntity<PlayerDTO> requestUpdate = new HttpEntity<>(updatedPlayerDTO);

        ResponseEntity<PlayerDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/players/" + playerResponse.getBody().getId(), HttpMethod.PUT, requestUpdate, PlayerDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Jane");
    }

    @Test
    void testDeletePlayer() {
        // Create a team first
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);
        assertThat(teamResponse.getStatusCodeValue()).isEqualTo(200);
        assertThat(teamResponse.getBody()).isNotNull();
        Long teamId = teamResponse.getBody().getId();
        assertThat(teamId).isNotNull();

        // Create a player associated with the team
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("John");
        playerDTO.setLastName("Doe");
        playerDTO.setPosition("Forward");
        playerDTO.setTeamId(teamId);
        ResponseEntity<PlayerDTO> playerResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/players", playerDTO, PlayerDTO.class);
        assertThat(playerResponse.getStatusCodeValue()).isEqualTo(200);
        assertThat(playerResponse.getBody()).isNotNull();
        Long playerId = playerResponse.getBody().getId();
        assertThat(playerId).isNotNull();

        // Delete the player
        restTemplate.withBasicAuth("admin", "admin123").delete("/api/players/" + playerId);

        // Try to get the deleted player
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/players/" + playerId, String.class);

        // Log the raw response
        System.out.println("Raw response: " + response.getBody());

        // Check the status code
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}
