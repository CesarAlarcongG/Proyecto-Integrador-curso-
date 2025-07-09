package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.AdministradorDTO;
import com.example.backendintegrador.service.AdministradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @PostMapping
    public ResponseEntity<AdministradorDTO> createAdministrador(@Valid @RequestBody AdministradorDTO administradorDTO) {
        AdministradorDTO savedAdministrador = administradorService.saveAdministrador(administradorDTO);
        return ResponseEntity.ok(savedAdministrador);
    }

    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> getAllAdministradores() {
        List<AdministradorDTO> administradores = administradorService.getAllAdministradores();
        return ResponseEntity.ok(administradores);
    }
    @GetMapping("/correo/{correo}")
    public ResponseEntity<AdministradorDTO> getAdministradorByCorreo(@PathVariable String correo) {
        AdministradorDTO administrador = administradorService.getAdministradorByCorreo(correo);
        return ResponseEntity.ok(administrador);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDTO> getAdministradorById(@PathVariable Integer id) {
        AdministradorDTO administrador = administradorService.getAdministradorById(id);
        return ResponseEntity.ok(administrador);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AdministradorDTO> updateAdministrador(@PathVariable Integer id, @Valid @RequestBody AdministradorDTO administradorDTO) {
        AdministradorDTO updatedAdministrador = administradorService.updateAdministrador(id, administradorDTO);
        return ResponseEntity.ok(updatedAdministrador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable Integer id) {
        administradorService.deleteAdministrador(id);
        return ResponseEntity.noContent().build();
    }
}