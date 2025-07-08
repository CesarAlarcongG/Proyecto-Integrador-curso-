package com.example.backendintegrador.service;


import com.example.backendintegrador.dto.ViajeDTO;
import com.example.backendintegrador.exception.BusNotFoundException;
import com.example.backendintegrador.exception.RutaNotFoundException;
import com.example.backendintegrador.exception.ViajeNotFoundException;
import com.example.backendintegrador.persistence.entity.Bus;
import com.example.backendintegrador.persistence.entity.Ruta;
import com.example.backendintegrador.persistence.entity.Viaje;
import com.example.backendintegrador.persistence.repository.BusRepository;
import com.example.backendintegrador.persistence.repository.RutaRepository;
import com.example.backendintegrador.persistence.repository.ViajeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ViajeDTO saveViaje(ViajeDTO viajeDTO) {
        Ruta ruta = rutaRepository.findById(viajeDTO.getIdRuta())
                .orElseThrow(() -> new RutaNotFoundException("Ruta no encontrada con id: " + viajeDTO.getIdRuta()));

        Bus bus = busRepository.findById(viajeDTO.getIdCarro())
                .orElseThrow(() -> new BusNotFoundException("Bus no encontrado con id: " + viajeDTO.getIdCarro()));

        Viaje viaje = modelMapper.map(viajeDTO, Viaje.class);
        viaje.setRuta(ruta);
        viaje.setBus(bus);
        Viaje savedViaje = viajeRepository.save(viaje);
        return modelMapper.map(savedViaje, ViajeDTO.class);
    }

    public List<ViajeDTO> getAllViajes() {
        return viajeRepository.findAll().stream()
                .map(viaje -> modelMapper.map(viaje, ViajeDTO.class))
                .collect(Collectors.toList());
    }

    public ViajeDTO getViajeById(Integer id) {
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new ViajeNotFoundException("Viaje no encontrado con id: " + id));
        return modelMapper.map(viaje, ViajeDTO.class);
    }

    public ViajeDTO updateViaje(Integer id, ViajeDTO viajeDTO) {
        Viaje existingViaje = viajeRepository.findById(id)
                .orElseThrow(() -> new ViajeNotFoundException("Viaje no encontrado con id: " + id));

        Ruta ruta = rutaRepository.findById(viajeDTO.getIdRuta())
                .orElseThrow(() -> new RutaNotFoundException("Ruta no encontrada con id: " + viajeDTO.getIdRuta()));

        Bus bus = busRepository.findById(viajeDTO.getIdCarro())
                .orElseThrow(() -> new BusNotFoundException("Bus no encontrado con id: " + viajeDTO.getIdCarro()));

        modelMapper.map(viajeDTO, existingViaje);
        existingViaje.setRuta(ruta);
        existingViaje.setBus(bus);
        Viaje updatedViaje = viajeRepository.save(existingViaje);
        return modelMapper.map(updatedViaje, ViajeDTO.class);
    }

    public void deleteViaje(Integer id) {
        if (!viajeRepository.existsById(id)) {
            throw new ViajeNotFoundException("Viaje no encontrado con id: " + id);
        }
        viajeRepository.deleteById(id);
    }

    public List<ViajeDTO> buscarViajesPorFechaYRuta(LocalDate fecha, Integer idRuta) {
        List<Viaje> viajes = viajeRepository.findByFechaSalidaAndRutaId(fecha, idRuta);
        return viajes.stream()
                .map(viaje -> modelMapper.map(viaje, ViajeDTO.class))
                .collect(Collectors.toList());
    }
}
