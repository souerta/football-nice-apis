package com.example.football.nice.apis.util;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.exception.DuplicateEntityException;
import com.example.football.nice.apis.exception.InvalidEntityException;
import com.example.football.nice.apis.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PlayerUtils {

    public static void checkForDuplicateNewPlayers(List<PlayerDTO> playerDTOs) {
        Set<String> playerNames = new HashSet<>();
        for (PlayerDTO playerDTO : playerDTOs) {
            String playerIdentifier = playerDTO.getFirstName() + " " + playerDTO.getLastName();
            if (!playerNames.add(playerIdentifier)) {
                throw new InvalidEntityException("Duplicate player detected: " + playerIdentifier);
            }
        }
    }

}
