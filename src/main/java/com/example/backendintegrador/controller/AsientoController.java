package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.AsientoDTO;
import com.example.backendintegrador.service.AsientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asientos")
public class AsientoController {

    @Autowired
    private AsientoService asientoService;

    @PostMapping
    public ResponseEntity<AsientoDTO> createAsiento(@Valid @RequestBody AsientoDTO asientoDTO) {
        AsientoDTO savedAsiento = asientoService.saveAsiento(asientoDTO);
        return ResponseEntity.ok(savedAsiento);
    }

    @GetMapping
    public ResponseEntity<List<AsientoDTO>> getAllAsientos() {
        List<AsientoDTO> asientos = asientoService.getAllAsientos();
        return ResponseEntity.ok(asientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsientoDTO> getAsientoById(@PathVariable Integer id) {
        AsientoDTO asiento = asientoService.getAsientoById(id);
        return ResponseEntity.ok(asiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsientoDTO> updateAsiento(@PathVariable Integer id, @Valid @RequestBody AsientoDTO asientoDTO) {
        AsientoDTO updatedAsiento = asientoService.updateAsiento(id, asientoDTO);
        return ResponseEntity.ok(updatedAsiento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsiento(@PathVariable Integer id) {
        asientoService.deleteAsiento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<AsientoDTO>> getAsientosByBusId(@PathVariable Integer busId) {
        List<AsientoDTO> asientos = asientoService.getAsientosByBusId(busId);
        return ResponseEntity.ok(asientos);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<AsientoDTO>> getAsientosDisponibles() {
        List<AsientoDTO> asientos = asientoService.getAsientosDisponibles();
        return ResponseEntity.ok(asientos);
    }
}
