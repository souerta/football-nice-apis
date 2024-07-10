package com.example.football.nice.apis.service;

import com.example.football.nice.apis.dto.TeamDTO;
import com.example.football.nice.apis.entity.Player;
import com.example.football.nice.apis.entity.Team;
import com.example.football.nice.apis.exception.DuplicateEntityException;
import com.example.football.nice.apis.exception.EntityNotFoundException;
import com.example.football.nice.apis.repository.PlayerRepository;
import com.example.football.nice.apis.repository.TeamRepository;
import com.example.football.nice.apis.util.DtoConversionUtils;
import com.example.football.nice.apis.util.PlayerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public List<TeamDTO> getAllTeams(int page, int size, String sortBy) {
        log.info("Fetching all teams with pagination and sorting");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Team> teamPage = teamRepository.findAll(pageable);
        return teamPage.stream().map(DtoConversionUtils::convertToTeamDTO).collect(Collectors.toList());
    }

    public TeamDTO getTeamById(Long id) {
        log.info("Fetching team with id {}", id);
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found with id " + id));
        return DtoConversionUtils.convertToTeamDTO(team);
    }

    public TeamDTO createTeam( @Valid TeamDTO teamDTO) {
        log.info("Creating new team");
        if (teamRepository.existsByName(teamDTO.getName())) {
            throw new DuplicateEntityException("Team already exists with name " + teamDTO.getName());
        }
        // Vérifier les doublons de joueurs
        if (teamDTO.getPlayers() != null) {
            PlayerUtils.checkForDuplicatePlayers(teamDTO.getPlayers());
        }
        Team team = new Team();
        DtoConversionUtils.updateTeamFromDTO(team, teamDTO);
        Team savedTeam = teamRepository.save(team);

        // Add players if provided
        if (teamDTO.getPlayers() != null) {
            List<Player> players = DtoConversionUtils.convertToPlayerList(teamDTO.getPlayers(), savedTeam);
            playerRepository.saveAll(players);
        }

        return DtoConversionUtils.convertToTeamDTO(savedTeam);
    }

    public TeamDTO updateTeam(Long id, @Valid TeamDTO teamDTO) {
        log.info("Updating team with id {}", id);
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found with id " + id));
        // Vérifier les doublons de joueurs
        if (teamDTO.getPlayers() != null) {
            PlayerUtils.checkForDuplicatePlayers(teamDTO.getPlayers());
        }
        DtoConversionUtils.updateTeamFromDTO(team, teamDTO);
        Team savedTeam = teamRepository.save(team);

        // Update players if provided
        if (teamDTO.getPlayers() != null) {
            playerRepository.deleteByTeamId(savedTeam.getId());
            List<Player> players = DtoConversionUtils.convertToPlayerList(teamDTO.getPlayers(), savedTeam);
            playerRepository.saveAll(players);
        }

        return DtoConversionUtils.convertToTeamDTO(savedTeam);
    }

    public void deleteTeam(Long id) {
        log.info("Deleting team with id {}", id);
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found with id " + id));
        teamRepository.delete(team);
    }
}
