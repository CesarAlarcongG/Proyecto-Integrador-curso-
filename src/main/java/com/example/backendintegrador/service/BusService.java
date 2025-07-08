package com.example.backendintegrador.service;

import com.example.backendintegrador.dto.BusDTO;
import com.example.backendintegrador.exception.BusNotFoundException;
import com.example.backendintegrador.exception.ConductorNotFoundException;
import com.example.backendintegrador.exception.DuplicatePlacaException;
import com.example.backendintegrador.persistence.entity.Bus;
import com.example.backendintegrador.persistence.entity.Conductor;
import com.example.backendintegrador.persistence.repository.BusRepository;
import com.example.backendintegrador.persistence.repository.ConductorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public BusDTO saveBus(BusDTO busDTO) {
        // Verificar si la placa ya existe
        if (busRepository.existsByPlaca(busDTO.getPlaca())) {
            throw new DuplicatePlacaException("Ya existe un bus con la placa: " + busDTO.getPlaca());
        }

        Conductor conductor = conductorRepository.findById(busDTO.getIdConductor())
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con id: " + busDTO.getIdConductor()));

        Bus bus = modelMapper.map(busDTO, Bus.class);
        bus.setConductor(conductor);
        Bus savedBus = busRepository.save(bus);
        return modelMapper.map(savedBus, BusDTO.class);
    }

    public List<BusDTO> getAllBuses() {
        return busRepository.findAll().stream()
                .map(bus -> modelMapper.map(bus, BusDTO.class))
                .collect(Collectors.toList());
    }

    public BusDTO getBusById(Integer id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException("Bus no encontrado con id: " + id));
        return modelMapper.map(bus, BusDTO.class);
    }

    public BusDTO getBusByPlaca(String placa) {
        Bus bus = busRepository.findByPlaca(placa)
                .orElseThrow(() -> new BusNotFoundException("Bus no encontrado con placa: " + placa));
        return modelMapper.map(bus, BusDTO.class);
    }

    public BusDTO updateBus(Integer id, BusDTO busDTO) {
        Bus existingBus = busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException("Bus no encontrado con id: " + id));

        // Verificar si la placa ya existe (excluyendo la actual)
        if (!existingBus.getPlaca().equals(busDTO.getPlaca()) &&
                busRepository.existsByPlaca(busDTO.getPlaca())) {
            throw new DuplicatePlacaException("Ya existe un bus con la placa: " + busDTO.getPlaca());
        }

        Conductor conductor = conductorRepository.findById(busDTO.getIdConductor())
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con id: " + busDTO.getIdConductor()));

        modelMapper.map(busDTO, existingBus);
        existingBus.setConductor(conductor);
        Bus updatedBus = busRepository.save(existingBus);
        return modelMapper.map(updatedBus, BusDTO.class);
    }

    public void deleteBus(Integer id) {
        if (!busRepository.existsById(id)) {
            throw new BusNotFoundException("Bus no encontrado con id: " + id);
        }
        busRepository.deleteById(id);
    }
}