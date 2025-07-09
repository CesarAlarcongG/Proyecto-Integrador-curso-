package com.example.backendintegrador.service;

import com.example.backendintegrador.dto.ConductorDTO;
import com.example.backendintegrador.exception.ConductorNotFoundException;
import com.example.backendintegrador.exception.DuplicateDniException;
import com.example.backendintegrador.exception.DuplicateLicenciaException;
import com.example.backendintegrador.persistence.entity.Conductor;
import com.example.backendintegrador.persistence.repository.ConductorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConductorService {

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ConductorDTO saveConductor(ConductorDTO conductorDTO) {
        // Verificar si el DNI ya existe
        if (conductorRepository.existsByDni(conductorDTO.getDni())) {
            throw new DuplicateDniException("Ya existe un conductor con el DNI: " + conductorDTO.getDni());
        }

        // Verificar si la licencia ya existe
        if (conductorRepository.existsByNumeroLicenciaConduccion(conductorDTO.getNumeroLicenciaConduccion())) {
            throw new DuplicateLicenciaException("Ya existe un conductor con la licencia: " + conductorDTO.getNumeroLicenciaConduccion());
        }

        Conductor conductor = modelMapper.map(conductorDTO, Conductor.class);
        Conductor savedConductor = conductorRepository.save(conductor);

        return modelMapper.map(savedConductor, ConductorDTO.class);
    }

    public List<ConductorDTO> getAllConductores() {
        return conductorRepository.findAll().stream()
                .map(conductor -> modelMapper.map(conductor, ConductorDTO.class))
                .collect(Collectors.toList());
    }

    public ConductorDTO getConductorById(Integer id) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con id: " + id));
        return modelMapper.map(conductor, ConductorDTO.class);
    }

    public ConductorDTO getConductorByDni(String dni) {
        Conductor conductor = conductorRepository.findByDni(dni)
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con DNI: " + dni));
        return modelMapper.map(conductor, ConductorDTO.class);
    }

    public ConductorDTO updateConductor(Integer id, ConductorDTO conductorDTO) {
        Conductor existingConductor = conductorRepository.findById(id)
                .orElseThrow(() -> new ConductorNotFoundException("Conductor no encontrado con id: " + id));

        // Verificar si el DNI ya existe (excluyendo el actual)
        if (!existingConductor.getDni().equals(conductorDTO.getDni()) &&
                conductorRepository.existsByDni(conductorDTO.getDni())) {
            throw new DuplicateDniException("Ya existe un conductor con el DNI: " + conductorDTO.getDni());
        }

        // Verificar si la licencia ya existe (excluyendo la actual)
        if (!existingConductor.getNumeroLicenciaConduccion().equals(conductorDTO.getNumeroLicenciaConduccion()) &&
                conductorRepository.existsByNumeroLicenciaConduccion(conductorDTO.getNumeroLicenciaConduccion())) {
            throw new DuplicateLicenciaException("Ya existe un conductor con la licencia: " + conductorDTO.getNumeroLicenciaConduccion());
        }

        modelMapper.map(conductorDTO, existingConductor);
        Conductor updatedConductor = conductorRepository.save(existingConductor);
        return modelMapper.map(updatedConductor, ConductorDTO.class);
    }

    public void deleteConductor(Integer id) {
        if (!conductorRepository.existsById(id)) {
            throw new ConductorNotFoundException("Conductor no encontrado con id: " + id);
        }
        conductorRepository.deleteById(id);
    }


}
