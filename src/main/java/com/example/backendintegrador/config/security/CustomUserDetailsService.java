package com.example.backendintegrador.config.security;

import com.example.backendintegrador.exception.AdministradorNotFoundException;
import com.example.backendintegrador.persistence.entity.Administrador;
import com.example.backendintegrador.persistence.repository.AdministradorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdministradorRepository administradorRepository;

    public CustomUserDetailsService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Administrador administrador = administradorRepository.findByCorreo(correo)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador no encontrado con correo: " + correo));

        return new org.springframework.security.core.userdetails.User(
                administrador.getCorreo(),
                administrador.getContrasena(),
                administrador.getAuthorities()
        );
    }
}
