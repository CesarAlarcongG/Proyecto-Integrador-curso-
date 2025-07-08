package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
}
