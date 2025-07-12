package com.example.backendintegrador.service;

import com.example.backendintegrador.dto.PasajeDTO;
import com.example.backendintegrador.exception.*;
import com.example.backendintegrador.persistence.entity.*;
import com.example.backendintegrador.persistence.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasajeService {

    @Autowired
    private PasajeRepository pasajeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private AsientoRepository asientoRepository;

    @Autowired
    private UsuarioAsientoRepository usuarioAsientoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public PasajeDTO savePasaje(PasajeDTO pasajeDTO) {
        // Obtener el viaje
        Viaje viaje = viajeRepository.findById(pasajeDTO.getIdViaje())
                .orElseThrow(() -> new ViajeNotFoundException("Viaje no encontrado con id: " + pasajeDTO.getIdViaje()));

        // Obtener usuarios
        List<Usuario> usuarios = usuarioService.saveAllUsuario(pasajeDTO.getUsuarioDTOS());

        // Obtener ruta
        Ruta ruta = rutaRepository.findById(pasajeDTO.getIdRuta())
                .orElseThrow(() -> new RutaNotFoundException("Ruta no encontrada con id: " + pasajeDTO.getIdRuta()));

        // Verificación de tamaño de listas
        List<Integer> asientoIds = pasajeDTO.getAsientosIds();
        if (asientoIds.size() != usuarios.size()) {
            throw new IllegalArgumentException("La cantidad de usuarios y asientos debe ser igual");
        }

        // Crear y guardar pasaje
        Pasaje pasaje = modelMapper.map(pasajeDTO, Pasaje.class);
        pasaje.setUsuario(usuarios);
        pasaje.setRuta(ruta);
        pasaje.setViaje(viaje);
        Pasaje savedPasaje = pasajeRepository.save(pasaje);

        // Crear y guardar relación usuario-asiento
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            Integer asientoId = asientoIds.get(i);

            Asiento asiento = asientoRepository.findById(asientoId)
                    .orElseThrow(() -> new AsientoNotFoundException("Asiento no encontrado con id: " + asientoId));

            if (!"disponible".equalsIgnoreCase(asiento.getEstado())) {
                throw new AsientoNoDisponibleException("El asiento con id " + asientoId + " no está disponible");
            }

            UsuarioAsiento usuarioAsiento = new UsuarioAsiento();
            usuarioAsiento.setUsuario(usuario);
            usuarioAsiento.setAsiento(asiento);
            usuarioAsiento.setPasaje(savedPasaje);
            usuarioAsientoRepository.save(usuarioAsiento);

            asiento.setEstado("ocupado");
            asientoRepository.save(asiento);
        }

        return modelMapper.map(savedPasaje, PasajeDTO.class);
    }


    public List<PasajeDTO> getAllPasajes() {
        return pasajeRepository.findAll().stream()
                .map(pasaje -> modelMapper.map(pasaje, PasajeDTO.class))
                .collect(Collectors.toList());
    }

    public PasajeDTO getPasajeById(Integer id) {
        Pasaje pasaje = pasajeRepository.findById(id)
                .orElseThrow(() -> new PasajeNotFoundException("Pasaje no encontrado con id: " + id));
        return modelMapper.map(pasaje, PasajeDTO.class);
    }

    @Transactional
    public void deletePasaje(Integer id) {
        Pasaje pasaje = pasajeRepository.findById(id)
                .orElseThrow(() -> new PasajeNotFoundException("Pasaje no encontrado con id: " + id));

        // Liberar asientos
        List<UsuarioAsiento> usuarioAsientos = usuarioAsientoRepository.findByPasajeIdPasaje(id);
        for (UsuarioAsiento usuarioAsiento : usuarioAsientos) {
            Asiento asiento = usuarioAsiento.getAsiento();
            asiento.setEstado("disponible");
            asientoRepository.save(asiento);
        }

        // Eliminar relaciones Usuario-Asiento
        usuarioAsientoRepository.deleteByPasajeIdPasaje(id);

        // Eliminar pasaje
        pasajeRepository.delete(pasaje);
    }

    public List<PasajeDTO> getPasajesByUsuarioId(Integer usuarioId) {
        return pasajeRepository.findByUsuario_IdUsuario(usuarioId).stream()
                .map(pasaje -> modelMapper.map(pasaje, PasajeDTO.class))
                .collect(Collectors.toList());
    }

}