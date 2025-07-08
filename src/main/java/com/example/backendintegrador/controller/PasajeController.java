package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.PasajeDTO;
import com.example.backendintegrador.service.PasajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajes")
public class PasajeController {

    @Autowired
    private PasajeService pasajeService;

    @PostMapping
    public ResponseEntity<PasajeDTO> createPasaje(@Valid @RequestBody PasajeDTO pasajeDTO) {
        PasajeDTO savedPasaje = pasajeService.savePasaje(pasajeDTO);
        return ResponseEntity.ok(savedPasaje);
    }

    @GetMapping
    public ResponseEntity<List<PasajeDTO>> getAllPasajes() {
        List<PasajeDTO> pasajes = pasajeService.getAllPasajes();
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeDTO> getPasajeById(@PathVariable Integer id) {
        PasajeDTO pasaje = pasajeService.getPasajeById(id);
        return ResponseEntity.ok(pasaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasaje(@PathVariable Integer id) {
        pasajeService.deletePasaje(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PasajeDTO>> getPasajesByUsuarioId(@PathVariable Integer usuarioId) {
        List<PasajeDTO> pasajes = pasajeService.getPasajesByUsuarioId(usuarioId);
        return ResponseEntity.ok(pasajes);
    }
}
