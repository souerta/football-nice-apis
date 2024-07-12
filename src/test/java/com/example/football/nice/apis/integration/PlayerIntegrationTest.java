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

import java.util.List;

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
        TeamDTO teamDTO = createTestTeamDTO();
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        PlayerDTO playerDTO = createTestPlayerDTO();
        playerDTO.setTeamId(teamResponse.getBody().getId());

        ResponseEntity<PlayerDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/players", playerDTO, PlayerDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
    }

    @Test
    void testGetPlayerById() {
        TeamDTO teamDTO = createTestTeamDTO();
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        PlayerDTO playerDTO = createTestPlayerDTO();
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
        TeamDTO teamDTO = createTestTeamDTO();
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        PlayerDTO playerDTO = createTestPlayerDTO();
        playerDTO.setTeamId(teamResponse.getBody().getId());
        ResponseEntity<PlayerDTO> playerResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/players", playerDTO, PlayerDTO.class);

        PlayerDTO updatedPlayerDTO = new PlayerDTO();
        updatedPlayerDTO.setFirstName("Jane");
        updatedPlayerDTO.setLastName("Doe");
        updatedPlayerDTO.setPosition("Midfielder");
        updatedPlayerDTO.setAge(26);
        updatedPlayerDTO.setJerseyNumber(12);
        updatedPlayerDTO.setNationality("Canadian");
        updatedPlayerDTO.setSize("5ft 8in");
        updatedPlayerDTO.setSalary(55000.0);

        HttpEntity<PlayerDTO> requestUpdate = new HttpEntity<>(updatedPlayerDTO);
        ResponseEntity<PlayerDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/players/" + playerResponse.getBody().getId(), HttpMethod.PUT, requestUpdate, PlayerDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Jane");
    }

    @Test
    void testDeletePlayer() {
        TeamDTO teamDTO = createTestTeamDTO();
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        PlayerDTO playerDTO = createTestPlayerDTO();
        playerDTO.setTeamId(teamResponse.getBody().getId());
        ResponseEntity<PlayerDTO> playerResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/players", playerDTO, PlayerDTO.class);

        restTemplate.withBasicAuth("admin", "admin123").delete("/api/players/" + playerResponse.getBody().getId());

        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/players/" + playerResponse.getBody().getId(), String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }



    private TeamDTO createTestTeamDTO() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        return teamDTO;
    }

    private PlayerDTO createTestPlayerDTO() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("John");
        playerDTO.setLastName("Doe");
        playerDTO.setPosition("Forward");
        playerDTO.setAge(25);
        playerDTO.setJerseyNumber(10);
        playerDTO.setNationality("American");
        playerDTO.setSize("6ft");
        playerDTO.setSalary(50000.0);
        return playerDTO;
    }
}
