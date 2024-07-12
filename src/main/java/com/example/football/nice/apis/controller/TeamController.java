package com.example.football.nice.apis.controller;

import com.example.football.nice.apis.dto.TeamDTO;
import com.example.football.nice.apis.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * TeamController gère les endpoints REST pour les opérations CRUD sur les équipes.
 */
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;
    /**
     * Récupère la liste paginée et triée de toutes les équipes.
     *
     * @param page   le numéro de la page à récupérer.
     * @param size   le nombre d'éléments par page.
     * @param sortBy le champ sur lequel trier les résultats.
     * @return ResponseEntity contenant la liste paginée et triée des équipes.
     */
    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        List<TeamDTO> teams = teamService.getAllTeams(page, size, sortBy);
        return ResponseEntity.ok(teams);
    }
    /**
     * Récupère une équipe par son ID.
     *
     * @param id l'identifiant de l'équipe.
     * @return ResponseEntity contenant l'équipe correspondant à l'ID fourni.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        TeamDTO team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }
    /**
     * Crée une nouvelle équipe.
     *
     * @param teamDTO les informations de l'équipe à créer.
     * @return ResponseEntity contenant l'équipe créée.
     */
    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO) {
        TeamDTO createdTeam = teamService.createTeam(teamDTO);
        return ResponseEntity.ok(createdTeam);
    }
    /**
     * Met à jour une équipe existante.
     *
     * @param id      l'identifiant de l'équipe à mettre à jour.
     * @param teamDTO les nouvelles informations de l'équipe.
     * @return ResponseEntity contenant l'équipe mise à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@Valid @PathVariable Long id, @RequestBody TeamDTO teamDTO) {
        TeamDTO updatedTeam = teamService.updateTeam(id, teamDTO);
        return ResponseEntity.ok(updatedTeam);
    }
    /**
     * Supprime une équipe par son ID.
     *
     * @param id l'identifiant de l'équipe à supprimer.
     * @return ResponseEntity sans contenu indiquant que la suppression a réussi.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
