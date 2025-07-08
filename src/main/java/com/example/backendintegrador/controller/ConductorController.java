package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.ConductorDTO;
import com.example.backendintegrador.service.ConductorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conductores")
public class ConductorController {

    @Autowired
    private ConductorService conductorService;

    @PostMapping
    public ResponseEntity<ConductorDTO> createConductor(@Valid @RequestBody ConductorDTO conductorDTO) {
        ConductorDTO savedConductor = conductorService.saveConductor(conductorDTO);
        return ResponseEntity.ok(savedConductor);
    }

    @GetMapping
    public ResponseEntity<List<ConductorDTO>> getAllConductores() {
        List<ConductorDTO> conductores = conductorService.getAllConductores();
        return ResponseEntity.ok(conductores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorDTO> getConductorById(@PathVariable Integer id) {
        ConductorDTO conductor = conductorService.getConductorById(id);
        return ResponseEntity.ok(conductor);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<ConductorDTO> getConductorByDni(@PathVariable String dni) {
        ConductorDTO conductor = conductorService.getConductorByDni(dni);
        return ResponseEntity.ok(conductor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConductorDTO> updateConductor(@PathVariable Integer id, @Valid @RequestBody ConductorDTO conductorDTO) {
        ConductorDTO updatedConductor = conductorService.updateConductor(id, conductorDTO);
        return ResponseEntity.ok(updatedConductor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConductor(@PathVariable Integer id) {
        conductorService.deleteConductor(id);
        return ResponseEntity.noContent().build();
    }
}
