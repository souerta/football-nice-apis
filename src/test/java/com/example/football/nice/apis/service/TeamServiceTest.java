package com.example.football.nice.apis.service;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.dto.TeamDTO;
import com.example.football.nice.apis.entity.Team;
import com.example.football.nice.apis.exception.DuplicateEntityException;
import com.example.football.nice.apis.exception.EntityNotFoundException;
import com.example.football.nice.apis.exception.InvalidEntityException;
import com.example.football.nice.apis.repository.PlayerRepository;
import com.example.football.nice.apis.repository.TeamRepository;
import com.example.football.nice.apis.util.DtoConversionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private Team team;
    private TeamDTO teamDTO;

    @Mock
    private PlayerRepository playerRepository;
    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setName("Team A");
        team.setAcronym("TA");
        team.setBudget(1000000.0);

        teamDTO = new TeamDTO();
        teamDTO.setName("Team A");
        teamDTO.setAcronym("TA");
        teamDTO.setBudget(1000000.0);
    }

    @Test
    void testCreateTeam() {
        when(teamRepository.existsByName(anyString())).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        TeamDTO createdTeam = teamService.createTeam(teamDTO);

        assertNotNull(createdTeam);
        assertEquals(team.getName(), createdTeam.getName());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void testCreateTeam_Duplicate() {
        when(teamRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> teamService.createTeam(teamDTO));
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void testGetTeamById() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        TeamDTO foundTeam = teamService.getTeamById(1L);

        assertNotNull(foundTeam);
        assertEquals(team.getName(), foundTeam.getName());
        verify(teamRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetTeamById_NotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.getTeamById(1L));
        verify(teamRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateTeam() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        TeamDTO updatedTeam = teamService.updateTeam(1L, teamDTO);

        assertNotNull(updatedTeam);
        assertEquals(team.getName(), updatedTeam.getName());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void testUpdateTeam_NotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.updateTeam(1L, teamDTO));
        verify(teamRepository, never()).save(any(Team.class));
    }



    @Test
    void testCreateTeamWithPlayers() {
        // Arrange
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team Name");
        teamDTO.setAcronym("TN");
        teamDTO.setBudget(100000.0);

        List<PlayerDTO> playerDTOList = new ArrayList<>();
        PlayerDTO playerDTO1 = new PlayerDTO();
        playerDTO1.setFirstName("John");
        playerDTO1.setLastName("Doe");
        playerDTO1.setPosition("Forward");
        playerDTOList.add(playerDTO1);

        PlayerDTO playerDTO2 = new PlayerDTO();
        playerDTO2.setFirstName("Jane");
        playerDTO2.setLastName("Doe");
        playerDTO2.setPosition("Midfielder");
        playerDTOList.add(playerDTO2);

        teamDTO.setPlayers(playerDTOList);

        Team team = new Team();
        team.setId(1L);
        team.setName(teamDTO.getName());
        team.setAcronym(teamDTO.getAcronym());
        team.setBudget(teamDTO.getBudget());
        team.setPlayers(DtoConversionUtils.convertToPlayerList(playerDTOList, team));

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // Act
        TeamDTO createdTeamDTO = teamService.createTeam(teamDTO);

        // Assert
        assertThat(createdTeamDTO).isNotNull();
        assertThat(createdTeamDTO.getPlayers()).hasSize(2);

        verify(teamRepository, times(1)).save(any(Team.class));
        verify(playerRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdateTeamWithPlayers() {
        // Arrange
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Updated Team Name");
        teamDTO.setAcronym("UTN");
        teamDTO.setBudget(200000.0);

        List<PlayerDTO> playerDTOList = new ArrayList<>();
        PlayerDTO playerDTO1 = new PlayerDTO();
        playerDTO1.setFirstName("John");
        playerDTO1.setLastName("Doe");
        playerDTO1.setPosition("Forward");
        playerDTOList.add(playerDTO1);

        PlayerDTO playerDTO2 = new PlayerDTO();
        playerDTO2.setFirstName("Jane");
        playerDTO2.setLastName("Doe");
        playerDTO2.setPosition("Midfielder");
        playerDTOList.add(playerDTO2);

        teamDTO.setPlayers(playerDTOList);

        Team existingTeam = new Team();
        existingTeam.setId(1L);
        existingTeam.setName("Old Team Name");
        existingTeam.setAcronym("OTN");
        existingTeam.setBudget(100000.0);
        existingTeam.setPlayers(new ArrayList<>());

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(existingTeam);

        // Act
        TeamDTO updatedTeamDTO = teamService.updateTeam(1L, teamDTO);

        // Assert
        assertThat(updatedTeamDTO).isNotNull();
        assertThat(updatedTeamDTO.getPlayers()).hasSize(2);

        verify(teamRepository, times(1)).findById(anyLong());
        verify(teamRepository, times(1)).save(any(Team.class));
        verify(playerRepository, times(1)).deleteByTeamId(anyLong());
        verify(playerRepository, times(1)).saveAll(anyList());
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

        assertThatThrownBy(() -> teamService.createTeam(teamDTO))
                .isInstanceOf(InvalidEntityException.class)
                .hasMessageContaining("Duplicate player detected: John Doe");
    }


    @Test
    void testUpdateTeamWithDuplicatePlayers() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("Team A");
        teamDTO.setAcronym("TA");
        teamDTO.setBudget(100000.0);

        PlayerDTO player1 = new PlayerDTO();
        player1.setFirstName("John");
        player1.setLastName("Doe");

        PlayerDTO player2 = new PlayerDTO();
        player2.setFirstName("John");
        player2.setLastName("Doe");

        teamDTO.setPlayers(Arrays.asList(player1, player2));

        Team existingTeam = new Team();
        existingTeam.setId(1L);
        existingTeam.setName("Old Team Name");
        existingTeam.setAcronym("OTN");
        existingTeam.setBudget(100000.0);
        existingTeam.setPlayers(new ArrayList<>());

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(existingTeam));

        assertThrows(InvalidEntityException.class, () -> teamService.updateTeam(1L, teamDTO));

        verify(teamRepository, times(1)).findById(anyLong());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void testDeleteTeam() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        teamService.deleteTeam(1L);

        verify(teamRepository, times(1)).delete(any(Team.class));
    }
    @Test
    void testDeleteTeam_NotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.deleteTeam(1L));
        verify(teamRepository, never()).delete(any(Team.class));
    }
}
