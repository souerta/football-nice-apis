package com.example.football.nice.apis.service;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.entity.Player;
import com.example.football.nice.apis.exception.DuplicateEntityException;
import com.example.football.nice.apis.exception.EntityNotFoundException;
import com.example.football.nice.apis.repository.PlayerRepository;
import com.example.football.nice.apis.repository.TeamRepository;
import com.example.football.nice.apis.util.DtoConversionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player player;
    private PlayerDTO playerDTO;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setId(1L);
        player.setFirstName("Player 1");
        player.setLastName("Player 1 Name" );
        player.setPosition("12");

        playerDTO = new PlayerDTO();
        playerDTO.setFirstName("Player 2");
        playerDTO.setLastName("Player 2 Name");
        playerDTO.setPosition("13");
    }

    @Test
    void testCreatePlayer() {
        when(playerRepository.existsByFirstNameAndLastName(anyString(), anyString())).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        PlayerDTO createdPlayer = playerService.createPlayer(playerDTO);

        assertNotNull(createdPlayer);
        assertEquals(player.getFirstName(), createdPlayer.getFirstName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testCreatePlayer_Duplicate() {
        when(playerRepository.existsByFirstNameAndLastName(anyString(), anyString())).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> playerService.createPlayer(playerDTO));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testGetPlayerById() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));

        PlayerDTO foundPlayer = playerService.getPlayerById(1L);

        assertNotNull(foundPlayer);
        assertEquals(player.getFirstName(), foundPlayer.getFirstName());
        verify(playerRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetPlayerById_NotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> playerService.getPlayerById(1L));
        verify(playerRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdatePlayer() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        PlayerDTO updatedPlayer = playerService.updatePlayer(1L, playerDTO);

        assertNotNull(updatedPlayer);
        assertEquals(player.getFirstName(), updatedPlayer.getFirstName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testUpdatePlayer_NotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> playerService.updatePlayer(1L, playerDTO));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testDeletePlayer() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));

        playerService.deletePlayer(1L);

        verify(playerRepository, times(1)).delete(any(Player.class));
    }

    @Test
    void testDeletePlayer_NotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> playerService.deletePlayer(1L));
        verify(playerRepository, never()).delete(any(Player.class));
    }
}