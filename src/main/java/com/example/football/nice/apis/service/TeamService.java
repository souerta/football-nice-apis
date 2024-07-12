package com.example.football.nice.apis.service;

import com.example.football.nice.apis.dto.PlayerDTO;
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
import org.springframework.dao.DataIntegrityViolationException;
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

    public TeamDTO createTeam(@Valid TeamDTO teamDTO) {
        log.info("Creating new team");
        if (teamRepository.existsByName(teamDTO.getName())) {
            throw new DuplicateEntityException("Team already exists with name " + teamDTO.getName());
        }
        // Vérifier les doublons de joueurs dans la nouvelle list
        if (teamDTO.getPlayers() != null) {
            PlayerUtils.checkForDuplicateNewPlayers(teamDTO.getPlayers());
        }
        // Vérifier les doublons de joueurs dans la base
        List<PlayerDTO> players = teamDTO.getPlayers();
        if (players != null) {
            for (PlayerDTO player : players) {
                if (playerRepository.existsByFirstNameAndLastNameAndPositionAndJerseyNumberAndAgeAndNationalityAndSizeAndSalary(
                        player.getFirstName(), player.getLastName(), player.getPosition(), player.getJerseyNumber(),
                        player.getAge(), player.getNationality(), player.getSize(), player.getSalary())) {
                    throw new DuplicateEntityException("Player already exists in other team: " + player.getFirstName() + " " + player.getLastName());
                }
            }
        }
        try {
            Team team = new Team();
            DtoConversionUtils.updateTeamFromDTO(team, teamDTO);
            Team savedTeam = teamRepository.save(team);
            log.info("Team created successfully with id {}", savedTeam.getId());
            return DtoConversionUtils.convertToTeamDTO(savedTeam);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while creating team: {}", e.getMostSpecificCause().getMessage());
            throw new DataIntegrityViolationException("Data integrity violation: " + e.getMostSpecificCause().getMessage(), e);
        } catch (Exception e) {
            log.error("Error creating team: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while creating the team");
        }
    }

    public TeamDTO updateTeam(Long id, @Valid TeamDTO teamDTO) {
        log.info("Updating team with id {}", id);
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found with id " + id));

        // Vérifiez les doublons de joueurs dans la nouvelle liste
        if (teamDTO.getPlayers() != null) {
            PlayerUtils.checkForDuplicateNewPlayers(teamDTO.getPlayers());
        }
        try {
            // Suppression des joueurs existants
            team.getPlayers().clear();
            teamRepository.save(team);
            playerRepository.flush();

            // Mise à jour de l'équipe
            DtoConversionUtils.updateTeamFromDTO(team, teamDTO);
            Team savedTeam = teamRepository.save(team);
            return DtoConversionUtils.convertToTeamDTO(savedTeam);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while creating team: {}", e.getMostSpecificCause().getMessage());
            throw new DataIntegrityViolationException("Data integrity violation: " + e.getMostSpecificCause().getMessage(), e);
        } catch (Exception e) {
            log.error("Error creating team: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while creating the team");
        }
    }


    public void deleteTeam(Long id) {
        log.info("Deleting team with id {}", id);
        Team team = teamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Team not found with id " + id));
        teamRepository.delete(team);
    }
}
