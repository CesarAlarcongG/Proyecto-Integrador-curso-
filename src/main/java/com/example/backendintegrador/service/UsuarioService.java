package com.example.backendintegrador.service;

import com.example.backendintegrador.dto.UsuarioDTO;
import com.example.backendintegrador.exception.UsuarioNotFoundException;
import com.example.backendintegrador.persistence.entity.Usuario;
import com.example.backendintegrador.persistence.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(savedUsuario, UsuarioDTO.class);
    }

    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioDTO getUsuarioById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + id));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO getUsuarioByDni(String dni) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con DNI: " + dni));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO updateUsuario(Integer id, UsuarioDTO usuarioDTO) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + id));

        modelMapper.map(usuarioDTO, existingUsuario);
        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
        return modelMapper.map(updatedUsuario, UsuarioDTO.class);
    }

    public void deleteUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}