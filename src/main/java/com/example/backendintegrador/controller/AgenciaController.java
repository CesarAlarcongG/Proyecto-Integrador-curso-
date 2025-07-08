package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.AgenciaDTO;
import com.example.backendintegrador.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agencias")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @PostMapping
    public ResponseEntity<AgenciaDTO> createAgencia(@Valid @RequestBody AgenciaDTO agenciaDTO) {
        AgenciaDTO savedAgencia = agenciaService.saveAgencia(agenciaDTO);
        return ResponseEntity.ok(savedAgencia);
    }

    @GetMapping
    public ResponseEntity<List<AgenciaDTO>> getAllAgencias() {
        List<AgenciaDTO> agencias = agenciaService.getAllAgencias();
        return ResponseEntity.ok(agencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenciaDTO> getAgenciaById(@PathVariable Integer id) {
        AgenciaDTO agencia = agenciaService.getAgenciaById(id);
        return ResponseEntity.ok(agencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenciaDTO> updateAgencia(@PathVariable Integer id, @Valid @RequestBody AgenciaDTO agenciaDTO) {
        AgenciaDTO updatedAgencia = agenciaService.updateAgencia(id, agenciaDTO);
        return ResponseEntity.ok(updatedAgencia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgencia(@PathVariable Integer id) {
        agenciaService.deleteAgencia(id);
        return ResponseEntity.noContent().build();
    }
}
