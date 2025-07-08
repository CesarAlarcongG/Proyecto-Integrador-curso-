package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.UsuarioDTO;
import com.example.backendintegrador.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO savedUsuario = usuarioService.saveUsuario(usuarioDTO);
        return ResponseEntity.ok(savedUsuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<UsuarioDTO> getUsuarioByDni(@PathVariable String dni) {
        UsuarioDTO usuario = usuarioService.getUsuarioByDni(dni);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Integer id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioDTO);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}

