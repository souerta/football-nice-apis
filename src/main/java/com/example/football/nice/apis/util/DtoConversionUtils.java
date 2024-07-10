package com.example.football.nice.apis.util;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.dto.TeamDTO;
import com.example.football.nice.apis.entity.Player;
import com.example.football.nice.apis.entity.Team;
import com.example.football.nice.apis.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DtoConversionUtils {

    public static PlayerDTO convertToPlayerDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setFirstName(player.getFirstName());
        playerDTO.setLastName(player.getLastName());
        playerDTO.setPosition(player.getPosition());
        playerDTO.setJerseyNumber(player.getJerseyNumber());
        playerDTO.setAge(player.getAge());
        playerDTO.setNationality(player.getNationality());
        playerDTO.setSize(player.getSize());
        playerDTO.setSalary(player.getSalary());
        if (player.getTeam() != null) {
            playerDTO.setTeamId(player.getTeam().getId());
            playerDTO.setTeamName(player.getTeam().getName());
        }
        return playerDTO;
    }

    public static void updatePlayerFromDTO(Player player, PlayerDTO playerDTO, TeamRepository teamRepository) {
        player.setFirstName(playerDTO.getFirstName());
        player.setLastName(playerDTO.getLastName());
        player.setPosition(playerDTO.getPosition());
        player.setJerseyNumber(playerDTO.getJerseyNumber());
        player.setAge(playerDTO.getAge());
        player.setNationality(playerDTO.getNationality());
        player.setSize(playerDTO.getSize());
        player.setSalary(playerDTO.getSalary());
        if (playerDTO.getTeamId() != null) {
            player.setTeam(teamRepository.findById(playerDTO.getTeamId()).orElse(null));
        }
    }

    public static TeamDTO convertToTeamDTO(Team team) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setAcronym(team.getAcronym());
        teamDTO.setBudget(team.getBudget());
        teamDTO.setPlayers(team.getPlayers() != null
                ? team.getPlayers().stream().map(player -> {
            PlayerDTO playerDTO = convertToPlayerDTO(player);
            playerDTO.setTeamId(null);  // Exclude teamId
            playerDTO.setTeamName(null);  // Exclude teamName
            return playerDTO;
        }).collect(Collectors.toList())
                : new ArrayList<>());
        return teamDTO;
    }

    public static void updateTeamFromDTO(Team team, TeamDTO teamDTO) {
        team.setName(teamDTO.getName());
        team.setAcronym(teamDTO.getAcronym());
        team.setBudget(teamDTO.getBudget());
        // Update players if provided, otherwise retain existing players
        if (teamDTO.getPlayers() != null) {
            team.getPlayers().clear();
            team.getPlayers().addAll(teamDTO.getPlayers().stream().map(playerDTO -> {
                Player player = new Player();
                updatePlayerFromDTO(player, playerDTO, null);
                player.setTeam(team);  // Set the team
                return player;
            }).collect(Collectors.toList()));
        }
    }

    // New methods to handle player lists
    public static List<Player> convertToPlayerList(List<PlayerDTO> playerDTOs, Team team) {
        return playerDTOs.stream().map(playerDTO -> {
            Player player = new Player();
            updatePlayerFromDTO(player, playerDTO, null);
            player.setTeam(team);
            return player;
        }).collect(Collectors.toList());
    }

    public static void updatePlayersFromDTO(Team team, List<PlayerDTO> playerDTOs) {
        team.getPlayers().clear();
        team.getPlayers().addAll(convertToPlayerList(playerDTOs, team));
    }
}
