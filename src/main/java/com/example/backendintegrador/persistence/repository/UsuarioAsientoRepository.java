package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.UsuarioAsiento;

import java.util.List;

public interface UsuarioAsientoRepository {
    void save(UsuarioAsiento usuarioAsiento);

    List<UsuarioAsiento> findByPasajeId(Integer id);

    void deleteByPasajeId(Integer id);
}
