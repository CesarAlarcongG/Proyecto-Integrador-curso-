package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.RutaDTO;
import com.example.backendintegrador.service.RutaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

    @Autowired
    private RutaService rutaService;

    @PostMapping
    public ResponseEntity<RutaDTO> createRuta(@Valid @RequestBody RutaDTO rutaDTO) {
        RutaDTO savedRuta = rutaService.saveRuta(rutaDTO);
        return ResponseEntity.ok(savedRuta);
    }

    @GetMapping
    public ResponseEntity<List<RutaDTO>> getAllRutas() {
        List<RutaDTO> rutas = rutaService.getAllRutas();
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutaDTO> getRutaById(@PathVariable Integer id) {
        RutaDTO ruta = rutaService.getRutaById(id);
        return ResponseEntity.ok(ruta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutaDTO> updateRuta(@PathVariable Integer id, @Valid @RequestBody RutaDTO rutaDTO) {
        RutaDTO updatedRuta = rutaService.updateRuta(id, rutaDTO);
        return ResponseEntity.ok(updatedRuta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRuta(@PathVariable Integer id) {
        rutaService.deleteRuta(id);
        return ResponseEntity.noContent().build();
    }
}
