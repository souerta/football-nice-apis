package com.example.football.nice.apis.service;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.entity.Player;
import com.example.football.nice.apis.exception.DuplicateEntityException;
import com.example.football.nice.apis.exception.EntityNotFoundException;
import com.example.football.nice.apis.repository.PlayerRepository;
import com.example.football.nice.apis.repository.TeamRepository;
import com.example.football.nice.apis.util.DtoConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    public List<PlayerDTO> getAllPlayers() {
        log.info("Fetching all players");
        return playerRepository.findAll().stream().map(DtoConversionUtils::convertToPlayerDTO).collect(Collectors.toList());
    }

    public PlayerDTO getPlayerById(Long id) {
        log.info("Fetching player with id {}", id);
        Player player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Player not found with id " + id));
        return DtoConversionUtils.convertToPlayerDTO(player);
    }

    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        log.info("Creating new player");
        if (playerRepository.existsByFirstNameAndLastName(playerDTO.getFirstName(), playerDTO.getLastName())) {
            throw new DuplicateEntityException("Player already exists with name " + playerDTO.getFirstName() + " " + playerDTO.getLastName());
        }
        Player player = new Player();
        DtoConversionUtils.updatePlayerFromDTO(player, playerDTO, teamRepository);
        Player savedPlayer = playerRepository.save(player);
        return DtoConversionUtils.convertToPlayerDTO(savedPlayer);
    }

    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        log.info("Updating player with id {}", id);
        Player player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Player not found with id " + id));
        DtoConversionUtils.updatePlayerFromDTO(player, playerDTO, teamRepository);
        Player savedPlayer = playerRepository.save(player);
        return DtoConversionUtils.convertToPlayerDTO(savedPlayer);
    }

    public void deletePlayer(Long id) {
        log.info("Deleting player with id {}", id);
        Player player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Player not found with id " + id));
        playerRepository.delete(player);
    }
}
