package com.example.backendintegrador.service;

import com.example.backendintegrador.dto.AsientoDTO;
import com.example.backendintegrador.exception.AsientoNotFoundException;
import com.example.backendintegrador.exception.BusNotFoundException;
import com.example.backendintegrador.persistence.entity.Asiento;
import com.example.backendintegrador.persistence.entity.Bus;
import com.example.backendintegrador.persistence.repository.AsientoRepository;
import com.example.backendintegrador.persistence.repository.BusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsientoService {

    @Autowired
    private AsientoRepository asientoRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AsientoDTO saveAsiento(AsientoDTO asientoDTO) {
        Bus bus = null;
        if (asientoDTO.getIdBus() != null) {
            bus = busRepository.findById(asientoDTO.getIdBus())
                    .orElseThrow(() -> new BusNotFoundException("Bus no encontrado con id: " + asientoDTO.getIdBus()));
        }

        Asiento asiento = modelMapper.map(asientoDTO, Asiento.class);
        asiento.setBus(bus);
        Asiento savedAsiento = asientoRepository.save(asiento);
        return modelMapper.map(savedAsiento, AsientoDTO.class);
    }

    public List<AsientoDTO> getAllAsientos() {
        return asientoRepository.findAll().stream()
                .map(asiento -> modelMapper.map(asiento, AsientoDTO.class))
                .collect(Collectors.toList());
    }

    public AsientoDTO getAsientoById(Integer id) {
        Asiento asiento = asientoRepository.findById(id)
                .orElseThrow(() -> new AsientoNotFoundException("Asiento no encontrado con id: " + id));
        return modelMapper.map(asiento, AsientoDTO.class);
    }

    public AsientoDTO updateAsiento(Integer id, AsientoDTO asientoDTO) {
        Asiento existingAsiento = asientoRepository.findById(id)
                .orElseThrow(() -> new AsientoNotFoundException("Asiento no encontrado con id: " + id));

        Bus bus = null;
        if (asientoDTO.getIdBus() != null) {
            bus = busRepository.findById(asientoDTO.getIdBus())
                    .orElseThrow(() -> new BusNotFoundException("Bus no encontrado con id: " + asientoDTO.getIdBus()));
        }

        modelMapper.map(asientoDTO, existingAsiento);
        existingAsiento.setBus(bus);
        Asiento updatedAsiento = asientoRepository.save(existingAsiento);
        return modelMapper.map(updatedAsiento, AsientoDTO.class);
    }

    public void deleteAsiento(Integer id) {
        if (!asientoRepository.existsById(id)) {
            throw new AsientoNotFoundException("Asiento no encontrado con id: " + id);
        }
        asientoRepository.deleteById(id);
    }

    public List<AsientoDTO> getAsientosByBusId(Integer busId) {
        return asientoRepository.findByBusId(busId).stream()
                .map(asiento -> modelMapper.map(asiento, AsientoDTO.class))
                .collect(Collectors.toList());
    }

    public List<AsientoDTO> getAsientosDisponibles() {
        return asientoRepository.findByEstado("disponible").stream()
                .map(asiento -> modelMapper.map(asiento, AsientoDTO.class))
                .collect(Collectors.toList());
    }
}
