package com.example.backendintegrador.service;


import com.example.backendintegrador.dto.*;
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

import java.util.*;
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

    @Autowired
    private ActividadService actividadService;

    @Transactional
    public RutaDTO saveRuta(RutaDTO rutaDTO) {
        Actividad actividad = actividadService.generarActividad(rutaDTO.getIdAdministrador(), "El administrador con id"+ rutaDTO.getIdAdministrador()+" generó una nueva ruta");

        Ruta ruta = modelMapper.map(rutaDTO, Ruta.class);
        ruta.setActividad(actividad);
        Ruta savedRuta = rutaRepository.save(ruta);

        // Guardar relaciones Ruta-Agencia
        if (rutaDTO.getAgenciasIds() != null ) {

            List<RutaAgencia> rutaAgencias = new ArrayList<>();
            for (int i = 0; i < rutaDTO.getAgenciasIds().size(); i++) {
                Integer agenciaId = rutaDTO.getAgenciasIds().get(i);
                Agencia agencia = agenciaRepository.findById(agenciaId)
                        .orElseThrow(() -> new AgenciaNotFoundException("Agencia no encontrada con id: " + agenciaId));

                RutaAgencia rutaAgencia = new RutaAgencia();
                rutaAgencia.setId(new RutaAgencia.RutaAgenciaId(savedRuta.getIdRuta(), agencia.getIdAgencia()));
                rutaAgencia.setRuta(savedRuta);
                rutaAgencia.setAgencia(agencia);
                rutaAgencia.setOrden(i);

                rutaAgencias.add(rutaAgencia);
            }

            rutaAgenciaRepository.saveAll(rutaAgencias);
        }

        return modelMapper.map(savedRuta, RutaDTO.class);
    }

    public List<RutaDTO> getAllRutas() {
        List<Ruta> rutas = rutaRepository.findAll();

        return rutas.stream().map(ruta -> {
            RutaDTO dto = modelMapper.map(ruta, RutaDTO.class);

            // Ordenar rutaAgencias por el campo 'orden'
            List<RutaAgencia> rutaAgenciasOrdenadas = ruta.getRutaAgencias().stream()
                    .sorted(Comparator.comparingInt(RutaAgencia::getOrden))
                    .collect(Collectors.toList());



            List<AgenciaDTO> agenciaDTOS = rutaAgenciasOrdenadas.stream()
                    .map(rutaAgencia -> {
                        Agencia agencia = rutaAgencia.getAgencia();
                        AgenciaDTO agenciaDTO = new AgenciaDTO();
                        agenciaDTO.setIdAgencia(agencia.getIdAgencia());
                        agenciaDTO.setDepartamento(agencia.getDepartamento());
                        agenciaDTO.setProvincia(agencia.getProvincia());
                        agenciaDTO.setDireccion(agencia.getDireccion());
                        agenciaDTO.setReferencia(agencia.getReferencia());
                        agenciaDTO.setOrden(rutaAgencia.getOrden());
                        return agenciaDTO;
                    })
                    .collect(Collectors.toList());

            dto.setAgenciaDTOS(agenciaDTOS);

            // Extraer solo IDs de las agencias
            dto.setAgenciasIds(
                    agenciaDTOS.stream()
                            .map(AgenciaDTO::getIdAgencia)
                            .collect(Collectors.toList())
            );


            if (ruta.getActividad() != null && ruta.getActividad().getAdministrador() != null) {
                dto.setIdAdministrador(ruta.getActividad().getAdministrador().getIdAdministrador());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public RutaDTO getRutaById(Integer idRuta) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con ID: " + idRuta));

        RutaDTO dto = modelMapper.map(ruta, RutaDTO.class);

        // Obtener agencias relacionadas a esta ruta
        List<AgenciaDTO> agenciaDTOS = ruta.getRutaAgencias().stream()
                .map(rutaAgencia -> {
                    Agencia agencia = rutaAgencia.getAgencia();

                    AgenciaDTO agenciaDTO = new AgenciaDTO();
                    agenciaDTO.setIdAgencia(agencia.getIdAgencia());
                    agenciaDTO.setDepartamento(agencia.getDepartamento());
                    agenciaDTO.setProvincia(agencia.getProvincia());
                    agenciaDTO.setDireccion(agencia.getDireccion());
                    agenciaDTO.setReferencia(agencia.getReferencia());
                    agenciaDTO.setOrden(rutaAgencia.getOrden());

                    return agenciaDTO;
                })
                .collect(Collectors.toList());

        dto.setAgenciaDTOS(agenciaDTOS);

        // Setear agenciasIds y ordenAgencias si deseas también
        dto.setAgenciasIds(agenciaDTOS.stream()
                .map(AgenciaDTO::getIdAgencia)
                .collect(Collectors.toList()));



        // Setear el id del administrador (evitando posibles null)
        if (ruta.getActividad() != null && ruta.getActividad().getAdministrador() != null) {
            dto.setIdAdministrador(ruta.getActividad().getAdministrador().getIdAdministrador());
        }

        return dto;
    }

    @Transactional
    public RutaDTO updateRuta(Integer id, RutaDTO rutaDTO) {
        Ruta existingRuta = rutaRepository.findById(id)
                .orElseThrow(() -> new RutaNotFoundException("Ruta no encontrada con id: " + id));

        Actividad actividad = actividadService.generarActividad(rutaDTO.getIdAdministrador(), "El administrador con id"+ rutaDTO.getIdAdministrador()+" actualizó la ruta");

        modelMapper.map(rutaDTO, existingRuta);
        existingRuta.setActividad(actividad);
        Ruta updatedRuta = rutaRepository.save(existingRuta);

        // Actualizar relaciones Ruta-Agencia
        if (rutaDTO.getAgenciasIds() != null ) {

            // Eliminar relaciones existentes
            rutaAgenciaRepository.deleteByRutaIdRuta(updatedRuta.getIdRuta());

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
                rutaAgencia.setOrden(i++);

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


    @Transactional(readOnly = true)
    public List<Ruta> obtenerTodasLasRutasConInformacionCompleta() {
        return rutaRepository.findAllWithRelations();
    }

    // Método para transformar las entidades a DTOs si es necesario
    public List<RutaDTO> obtenerTodasLasRutasDTO() {
        List<Ruta> rutas = obtenerTodasLasRutasConInformacionCompleta();
        return rutas.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private RutaDTO convertirADTO(Ruta ruta) {
        RutaDTO dto = new RutaDTO();
        dto.setIdRuta(ruta.getIdRuta());
        dto.setNombre(ruta.getNombre());
        /**
        // Convertir actividad
        if (ruta.getActividad() != null) {
            ActividadDTO actividadDTO = new ActividadDTO();
            actividadDTO.setIdActividad(ruta.getActividad().getIdActividad());
            actividadDTO.setFecha(ruta.getActividad().getFecha());
            actividadDTO.setHora(ruta.getActividad().getHora());
            actividadDTO.setDescripcion(ruta.getActividad().getDescripcion());
            dto.setActividad(actividadDTO);
        }
         */

        // Convertir agencias con su orden
        if (ruta.getRutaAgencias() != null) {
            List<AgenciaDTO> agencias = ruta.getRutaAgencias().stream()
                    .map(ra -> {
                        AgenciaDTO agenciaDTO = new AgenciaDTO();
                        agenciaDTO.setIdAgencia(ra.getAgencia().getIdAgencia());
                        agenciaDTO.setDepartamento(ra.getAgencia().getDepartamento());
                        agenciaDTO.setOrden(ra.getOrden());
                        return agenciaDTO;
                    })
                    .sorted(Comparator.comparing(AgenciaDTO::getOrden))
                    .collect(Collectors.toList());
            dto.setAgenciaDTOS(agencias);
        }
        /**
        // Convertir viajes
        if (ruta.getViajes() != null) {
            List<ViajeDTO> viajes = ruta.getViajes().stream()
                    .map(v -> {
                        ViajeDTO viajeDTO = new ViajeDTO();
                        viajeDTO.setHoraSalida(v.getHoraSalida());
                        viajeDTO.setFechaSalida(v.getFechaSalida());
                        viajeDTO.setCosto(v.getCosto());
                        // Info del bus si es necesario
                        return viajeDTO;
                    })
                    .collect(Collectors.toList());
            dto.setViajes(viajes);
        }
         */

        return dto;
    }


}
