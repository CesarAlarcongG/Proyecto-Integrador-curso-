package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.UsuarioAsiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioAsientoRepository extends JpaRepository<UsuarioAsiento, Integer> {

    List<UsuarioAsiento> findByPasajeIdPasaje(Integer id);

    void deleteByPasajeIdPasaje(Integer id);
}
