package com.example.backendintegrador.service;


import com.example.backendintegrador.dto.AgenciaDTO;
import com.example.backendintegrador.exception.AgenciaNotFoundException;
import com.example.backendintegrador.persistence.entity.Agencia;
import com.example.backendintegrador.persistence.repository.AgenciaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AgenciaDTO saveAgencia(AgenciaDTO agenciaDTO) {
        Agencia agencia = modelMapper.map(agenciaDTO, Agencia.class);
        Agencia savedAgencia = agenciaRepository.save(agencia);
        return modelMapper.map(savedAgencia, AgenciaDTO.class);
    }

    public List<AgenciaDTO> getAllAgencias() {
        return agenciaRepository.findAll().stream()
                .map(agencia -> modelMapper.map(agencia, AgenciaDTO.class))
                .collect(Collectors.toList());
    }

    public AgenciaDTO getAgenciaById(Integer id) {
        Agencia agencia = agenciaRepository.findById(id)
                .orElseThrow(() -> new AgenciaNotFoundException("Agencia no encontrada con id: " + id));
        return modelMapper.map(agencia, AgenciaDTO.class);
    }

    public AgenciaDTO updateAgencia(Integer id, AgenciaDTO agenciaDTO) {
        Agencia existingAgencia = agenciaRepository.findById(id)
                .orElseThrow(() -> new AgenciaNotFoundException("Agencia no encontrada con id: " + id));

        modelMapper.map(agenciaDTO, existingAgencia);
        Agencia updatedAgencia = agenciaRepository.save(existingAgencia);
        return modelMapper.map(updatedAgencia, AgenciaDTO.class);
    }

    public void deleteAgencia(Integer id) {
        if (!agenciaRepository.existsById(id)) {
            throw new AgenciaNotFoundException("Agencia no encontrada con id: " + id);
        }
        agenciaRepository.deleteById(id);
    }
}
