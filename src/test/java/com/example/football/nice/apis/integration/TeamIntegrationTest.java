package com.example.football.nice.apis.integration;

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
public class TeamIntegrationTest {

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
    void testCreateTeam() {
        TeamDTO teamDTO = createTestTeamDTO();
        ResponseEntity<TeamDTO> response = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Team Name");
    }

    @Test
    void testGetTeamById() {
        TeamDTO teamDTO = createTestTeamDTO();
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
        TeamDTO teamDTO = createTestTeamDTO();
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

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
    void testDeleteTeam() {
        TeamDTO teamDTO = createTestTeamDTO();
        ResponseEntity<TeamDTO> teamResponse = restTemplate.withBasicAuth("admin", "admin123")
                .postForEntity("/api/teams", teamDTO, TeamDTO.class);

        restTemplate.withBasicAuth("admin", "admin123").delete("/api/teams/" + teamResponse.getBody().getId());

        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/teams/" + teamResponse.getBody().getId(), String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllTeamsWithPaginationAndSorting() {
        for (int i = 1; i <= 5; i++) {
            TeamDTO teamDTO = new TeamDTO();
            teamDTO.setName("Team " + i);
            teamDTO.setAcronym("T" + i);
            teamDTO.setBudget(100000.0 * i);
            restTemplate.withBasicAuth("admin", "admin123")
                    .postForEntity("/api/teams", teamDTO, TeamDTO.class);
        }

        ResponseEntity<List> response = restTemplate.withBasicAuth("admin", "admin123")
                .getForEntity("/api/teams?page=0&size=3&sort=budget,desc", List.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
    }

    private TeamDTO createTestTeamDTO() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        return teamDTO;
    }
}
