package com.example.backendintegrador.service;


import com.example.backendintegrador.dto.RutaDTO;
import com.example.backendintegrador.exception.ActividadNotFoundException;
import com.example.backendintegrador.exception.AgenciaNotFoundException;
import com.example.backendintegrador.exception.RutaNotFoundException;
import com.example.backendintegrador.persistence.entity.Actividad;
import com.example.backendintegrador.persistence.entity.Agencia;
import com.example.backendintegrador.persistence.entity.Ruta;
import com.example.backendintegrador.persistence.entity.RutaAgencia;
import com.example.backendintegrador.persistence.repository.ActividadRepository;
import com.example.backendintegrador.persistence.repository.AgenciaRepository;
import com.example.backendintegrador.persistence.repository.RutaAgenciaRepository;
import com.example.backendintegrador.persistence.repository.RutaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RutaService {

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private RutaAgenciaRepository rutaAgenciaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public RutaDTO saveRuta(RutaDTO rutaDTO) {
        Actividad actividad = actividadRepository.findById(rutaDTO.getIdActividad())
                .orElseThrow(() -> new ActividadNotFoundException("Actividad no encontrada con id: " + rutaDTO.getIdActividad()));

        Ruta ruta = modelMapper.map(rutaDTO, Ruta.class);
        ruta.setActividad(actividad);
        Ruta savedRuta = rutaRepository.save(ruta);

        // Guardar relaciones Ruta-Agencia
        if (rutaDTO.getAgenciasIds() != null && rutaDTO.getOrdenAgencias() != null &&
                rutaDTO.getAgenciasIds().size() == rutaDTO.getOrdenAgencias().size()) {

            List<RutaAgencia> rutaAgencias = new ArrayList<>();
            for (int i = 0; i < rutaDTO.getAgenciasIds().size(); i++) {
                Integer agenciaId = rutaDTO.getAgenciasIds().get(i);
                Agencia agencia = agenciaRepository.findById(agenciaId)
                        .orElseThrow(() -> new AgenciaNotFoundException("Agencia no encontrada con id: " + agenciaId));

                RutaAgencia rutaAgencia = new RutaAgencia();
                rutaAgencia.setId(new RutaAgencia.RutaAgenciaId(savedRuta.getIdRuta(), agencia.getIdAgencia()));
                rutaAgencia.setRuta(savedRuta);
                rutaAgencia.setAgencia(agencia);
                rutaAgencia.setOrden(rutaDTO.getOrdenAgencias().get(i));

                rutaAgencias.add(rutaAgencia);
            }

            rutaAgenciaRepository.saveAll(rutaAgencias);
        }

        return modelMapper.map(savedRuta, RutaDTO.class);
    }

    public List<RutaDTO> getAllRutas() {
        return rutaRepository.findAll().stream()
                .map(ruta -> modelMapper.map(ruta, RutaDTO.class))
                .collect(Collectors.toList());
    }

    public RutaDTO getRutaById(Integer id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new RutaNotFoundException("Ruta no encontrada con id: " + id));
        return modelMapper.map(ruta, RutaDTO.class);
    }

    @Transactional
    public RutaDTO updateRuta(Integer id, RutaDTO rutaDTO) {
        Ruta existingRuta = rutaRepository.findById(id)
                .orElseThrow(() -> new RutaNotFoundException("Ruta no encontrada con id: " + id));

        Actividad actividad = actividadRepository.findById(rutaDTO.getIdActividad())
                .orElseThrow(() -> new ActividadNotFoundException("Actividad no encontrada con id: " + rutaDTO.getIdActividad()));

        modelMapper.map(rutaDTO, existingRuta);
        existingRuta.setActividad(actividad);
        Ruta updatedRuta = rutaRepository.save(existingRuta);

        // Actualizar relaciones Ruta-Agencia
        if (rutaDTO.getAgenciasIds() != null && rutaDTO.getOrdenAgencias() != null &&
                rutaDTO.getAgenciasIds().size() == rutaDTO.getOrdenAgencias().size()) {

            // Eliminar relaciones existentes
            rutaAgenciaRepository.deleteByRutaId(updatedRuta.getIdRuta());

            // Crear nuevas relaciones
            List<RutaAgencia> rutaAgencias = new ArrayList<>();
            for (int i = 0; i < rutaDTO.getAgenciasIds().size(); i++) {
                Integer agenciaId = rutaDTO.getAgenciasIds().get(i);
                Agencia agencia = agenciaRepository.findById(agenciaId)
                        .orElseThrow(() -> new AgenciaNotFoundException("Agencia no encontrada con id: " + agenciaId));

                RutaAgencia rutaAgencia = new RutaAgencia();
                rutaAgencia.setId(new RutaAgencia.RutaAgenciaId(updatedRuta.getIdRuta(), agencia.getIdAgencia()));
                rutaAgencia.setRuta(updatedRuta);
                rutaAgencia.setAgencia(agencia);
                rutaAgencia.setOrden(rutaDTO.getOrdenAgencias().get(i));

                rutaAgencias.add(rutaAgencia);
            }

            rutaAgenciaRepository.saveAll(rutaAgencias);
        }

        return modelMapper.map(updatedRuta, RutaDTO.class);
    }

    public void deleteRuta(Integer id) {
        if (!rutaRepository.existsById(id)) {
            throw new RutaNotFoundException("Ruta no encontrada con id: " + id);
        }
        rutaRepository.deleteById(id);
    }
}
