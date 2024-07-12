package com.example.football.nice.apis.controller;

import com.example.football.nice.apis.dto.PlayerDTO;
import com.example.football.nice.apis.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PlayerController gère les endpoints REST pour les opérations CRUD sur les joueurs.
 */
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    /**
     * Récupère la liste de tous les joueurs.
     *
     * @return ResponseEntity contenant la liste de tous les joueurs.
     */
    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }
    /**
     * Récupère un joueur par son ID.
     *
     * @param id l'identifiant du joueur.
     * @return ResponseEntity contenant le joueur correspondant à l'ID fourni.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        PlayerDTO player = playerService.getPlayerById(id);
        return ResponseEntity.ok(player);
    }
    /**
     * Crée un nouveau joueur.
     *
     * @param playerDTO les informations du joueur à créer.
     * @return ResponseEntity contenant le joueur créé.
     */
    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        PlayerDTO createdPlayer = playerService.createPlayer(playerDTO);
        return ResponseEntity.ok(createdPlayer);
    }
    /**
     * Met à jour un joueur existant.
     *
     * @param id l'identifiant du joueur à mettre à jour.
     * @param playerDTO les nouvelles informations du joueur.
     * @return ResponseEntity contenant le joueur mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody PlayerDTO playerDTO) {
        PlayerDTO updatedPlayer = playerService.updatePlayer(id, playerDTO);
        return ResponseEntity.ok(updatedPlayer);
    }
    /**
     * Supprime un joueur par son ID.
     *
     * @param id l'identifiant du joueur à supprimer.
     * @return ResponseEntity sans contenu indiquant que la suppression a réussi.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
