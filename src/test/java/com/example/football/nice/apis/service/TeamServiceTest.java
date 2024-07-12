package com.example.football.nice.apis.service;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.dto.TeamDTO;
import com.example.football.nice.apis.entity.Team;
import com.example.football.nice.apis.exception.DuplicateEntityException;
import com.example.football.nice.apis.exception.EntityNotFoundException;
import com.example.football.nice.apis.repository.PlayerRepository;
import com.example.football.nice.apis.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTeams() {
        Team team1 = createTestTeam();
        Team team2 = createTestTeam();
        team2.setName("Another Team");

        List<Team> teams = Arrays.asList(team1, team2);
        Page<Team> teamPage = new PageImpl<>(teams);

        when(teamRepository.findAll(any(Pageable.class))).thenReturn(teamPage);

        List<TeamDTO> foundTeams = teamService.getAllTeams(0, 10, "name");

        assertThat(foundTeams).isNotNull();
        assertThat(foundTeams.size()).isEqualTo(2);
        verify(teamRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetTeamById() {
        Team team = createTestTeam();
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        TeamDTO foundTeam = teamService.getTeamById(1L);

        assertThat(foundTeam).isNotNull();
        assertThat(foundTeam.getName()).isEqualTo("Team Name");
        verify(teamRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetTeamByIdNotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.getTeamById(1L));
    }

    @Test
    void testCreateTeam() {
        TeamDTO teamDTO = createTestTeamDTO();
        Team team = createTestTeam();

        when(teamRepository.existsByName(anyString())).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        TeamDTO createdTeam = teamService.createTeam(teamDTO);

        assertThat(createdTeam).isNotNull();
        assertThat(createdTeam.getName()).isEqualTo("Team Name");
        verify(teamRepository, times(1)).existsByName(anyString());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void testCreateTeamDuplicateName() {
        TeamDTO teamDTO = createTestTeamDTO();
        when(teamRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> teamService.createTeam(teamDTO));
    }

    @Test
    void testCreateTeamDataIntegrityViolation() {
        TeamDTO teamDTO = createTestTeamDTO();
        when(teamRepository.existsByName(anyString())).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> teamService.createTeam(teamDTO));
    }

    @Test
    void testUpdateTeam() {
        TeamDTO teamDTO = createTestTeamDTO();
        Team team = createTestTeam();
        team.setId(1L);

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        TeamDTO updatedTeam = teamService.updateTeam(1L, teamDTO);

        assertThat(updatedTeam).isNotNull();
        assertThat(updatedTeam.getName()).isEqualTo("Team Name");

        verify(teamRepository, times(1)).findById(anyLong());
        verify(teamRepository, times(2)).save(any(Team.class));
        verify(playerRepository, times(1)).flush();
    }


    @Test
    void testUpdateTeamNotFound() {
        TeamDTO teamDTO = createTestTeamDTO();
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.updateTeam(1L, teamDTO));
    }

    @Test
    void testUpdateTeamDataIntegrityViolation() {
        TeamDTO teamDTO = createTestTeamDTO();
        Team team = createTestTeam();
        team.setId(1L);

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> teamService.updateTeam(1L, teamDTO));
    }

    @Test
    void testDeleteTeam() {
        Team team = createTestTeam();
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        teamService.deleteTeam(1L);

        verify(teamRepository, times(1)).findById(anyLong());
        verify(teamRepository, times(1)).delete(any(Team.class));
    }

    @Test
    void testDeleteTeamNotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.deleteTeam(1L));
    }

    private TeamDTO createTestTeamDTO() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);
        teamDTO.setPlayers(Collections.singletonList(createTestPlayerDTO()));
        return teamDTO;
    }

    private Team createTestTeam() {
        Team team = new Team();
        team.setName("Team Name");
        team.setAcronym("TN");
        team.setBudget(100000.0);
        return team;
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
