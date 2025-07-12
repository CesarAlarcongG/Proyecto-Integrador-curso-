package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.ViajeBusquedaDTO;
import com.example.backendintegrador.dto.ViajeDTO;
import com.example.backendintegrador.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @PostMapping
    public ResponseEntity<ViajeDTO> createViaje(@Valid @RequestBody ViajeDTO viajeDTO) {
        ViajeDTO savedViaje = viajeService.saveViaje(viajeDTO);
        return ResponseEntity.ok(savedViaje);
    }

    @GetMapping
    public ResponseEntity<List<ViajeDTO>> getAllViajes() {
        List<ViajeDTO> viajes = viajeService.getAllViajes();
        return ResponseEntity.ok(viajes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajeDTO> getViajeById(@PathVariable Integer id) {
        ViajeDTO viaje = viajeService.getViajeById(id);
        return ResponseEntity.ok(viaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViajeDTO> updateViaje(@PathVariable Integer id, @Valid @RequestBody ViajeDTO viajeDTO) {
        ViajeDTO updatedViaje = viajeService.updateViaje(id, viajeDTO);
        return ResponseEntity.ok(updatedViaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViaje(@PathVariable Integer id) {
        viajeService.deleteViaje(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/buscar-viajes")
    public ResponseEntity<List<ViajeDTO>> buscarViajes( @Valid @RequestBody ViajeBusquedaDTO busquedaDTO) {
        List<ViajeDTO> viajes = null;

        if (busquedaDTO.getFecha() != null){
            viajes = viajeService.buscarViajesPorFechaYRuta(
                    busquedaDTO.getFecha(),
                    busquedaDTO.getNombreRuta()
            );
        }

        viajes = viajeService.buscarViajesPorNombreRuta(busquedaDTO.getNombreRuta());

        return ResponseEntity.ok(viajes);
    }
}
