package com.example.backendintegrador.service;

import com.example.backendintegrador.config.security.JwtTokenProvider;
import com.example.backendintegrador.dto.AuthResponseDTO;
import com.example.backendintegrador.dto.LoginDTO;
import com.example.backendintegrador.exception.UnauthorizedException;
import com.example.backendintegrador.persistence.entity.Administrador;
import com.example.backendintegrador.persistence.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AdministradorRepository administradorRepository;

    public AuthResponseDTO login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getCorreo(),
                            loginDTO.getContrasena()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            Administrador administrador = administradorRepository.findByCorreo(loginDTO.getCorreo())
                    .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

            return new AuthResponseDTO(
                    token,
                    "Bearer",
                    administrador.getIdAdministrador(),
                    administrador.getNombre(),
                    administrador.getApellido(),
                    administrador.getCorreo(),
                    "ADMIN"
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
    }
}