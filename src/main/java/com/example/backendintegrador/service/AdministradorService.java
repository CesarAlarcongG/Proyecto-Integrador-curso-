package com.example.backendintegrador.service;

import com.example.backendintegrador.dto.AdministradorDTO;
import com.example.backendintegrador.exception.AdministradorNotFoundException;
import com.example.backendintegrador.exception.DuplicateCorreoException;
import com.example.backendintegrador.persistence.entity.Administrador;
import com.example.backendintegrador.persistence.repository.AdministradorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public AdministradorDTO saveAdministrador(AdministradorDTO administradorDTO) {
        // Verificar si el correo ya existe
        if (administradorRepository.existsByCorreo(administradorDTO.getCorreo())) {
            throw new DuplicateCorreoException("Ya existe un administrador con el correo: " + administradorDTO.getCorreo());
        }

        Administrador administrador = modelMapper.map(administradorDTO, Administrador.class);
        administrador.setContrasena(passwordEncoder.encode(administradorDTO.getContrasena()));
        Administrador savedAdministrador = administradorRepository.save(administrador);
        return modelMapper.map(savedAdministrador, AdministradorDTO.class);
    }

    public List<AdministradorDTO> getAllAdministradores() {
        return administradorRepository.findAll().stream()
                .map(administrador -> modelMapper.map(administrador, AdministradorDTO.class))
                .collect(Collectors.toList());
    }

    public AdministradorDTO getAdministradorById(Integer id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador no encontrado con id: " + id));
        return modelMapper.map(administrador, AdministradorDTO.class);
    }

    public AdministradorDTO getAdministradorByCorreo(String correo) {
        Administrador administrador = administradorRepository.findByCorreo(correo)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador no encontrado con correo: " + correo));
        return modelMapper.map(administrador, AdministradorDTO.class);
    }

    public AdministradorDTO updateAdministrador(Integer id, AdministradorDTO administradorDTO) {
        Administrador existingAdministrador = administradorRepository.findById(id)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador no encontrado con id: " + id));

        // Verificar si el correo ya existe (excluyendo el actual)
        if (!existingAdministrador.getCorreo().equals(administradorDTO.getCorreo()) &&
                administradorRepository.existsByCorreo(administradorDTO.getCorreo())) {
            throw new DuplicateCorreoException("Ya existe un administrador con el correo: " + administradorDTO.getCorreo());
        }

        modelMapper.map(administradorDTO, existingAdministrador);
        if (administradorDTO.getContrasena() != null && !administradorDTO.getContrasena().isEmpty()) {
            existingAdministrador.setContrasena(passwordEncoder.encode(administradorDTO.getContrasena()));
        }
        Administrador updatedAdministrador = administradorRepository.save(existingAdministrador);
        return modelMapper.map(updatedAdministrador, AdministradorDTO.class);
    }

    public void deleteAdministrador(Integer id) {
        if (!administradorRepository.existsById(id)) {
            throw new AdministradorNotFoundException("Administrador no encontrado con id: " + id);
        }
        administradorRepository.deleteById(id);
    }
}
