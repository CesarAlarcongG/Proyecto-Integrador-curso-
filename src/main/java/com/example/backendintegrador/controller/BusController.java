package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.BusDTO;
import com.example.backendintegrador.service.BusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping
    public ResponseEntity<BusDTO> createBus(@Valid @RequestBody BusDTO busDTO) {
        BusDTO savedBus = busService.saveBus(busDTO);
        return ResponseEntity.ok(savedBus);
    }

    @GetMapping
    public ResponseEntity<List<BusDTO>> getAllBuses() {
        List<BusDTO> buses = busService.getAllBuses();
        return ResponseEntity.ok(buses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> getBusById(@PathVariable Integer id) {
        BusDTO bus = busService.getBusById(id);
        return ResponseEntity.ok(bus);
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<BusDTO> getBusByPlaca(@PathVariable String placa) {
        BusDTO bus = busService.getBusByPlaca(placa);
        return ResponseEntity.ok(bus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusDTO> updateBus(@PathVariable Integer id, @Valid @RequestBody BusDTO busDTO) {
        BusDTO updatedBus = busService.updateBus(id, busDTO);
        return ResponseEntity.ok(updatedBus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Integer id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }
}
