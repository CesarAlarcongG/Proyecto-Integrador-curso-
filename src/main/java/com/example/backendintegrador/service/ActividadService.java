package com.example.backendintegrador.service;

import com.example.backendintegrador.persistence.entity.Actividad;
import com.example.backendintegrador.persistence.entity.Administrador;
import com.example.backendintegrador.persistence.repository.ActividadRepository;
import com.example.backendintegrador.persistence.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    public Actividad generarActividad(Integer idAdministrador, String descripción) {
        Administrador admin = administradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        Actividad actividad = Actividad.builder()
                .fecha(LocalDate.now())
                .hora(LocalTime.now())
                .descripcion(descripción)
                .administrador(admin)
                .build();
        actividadRepository.save(actividad);
        return actividad;
    }
}
