package com.example.backendintegrador.config.data;

import com.example.backendintegrador.persistence.entity.Administrador;
import com.example.backendintegrador.persistence.repository.AdministradorRepository;
import com.example.backendintegrador.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DatosIniciales {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner administrador(AdministradorRepository administradorRepository) {


        return args -> {
            if (administradorRepository.count() == 0) {
                Administrador usuario = Administrador.builder()
                        .nombre("Super")
                        .apellido("Admin")
                        .correo("root@root.root")
                        .contrasena(passwordEncoder.encode("root12345678"))
                        .roles(List.of("ROLE_ADMIN", "ROLE_USER"))
                        .build();

                administradorRepository.save(usuario);

                System.out.println("✅ Datos iniciales cargados correctamente:" +
                        "\n📧 Usuario: root@root.root" +
                        "\n🔐 Contraseña: root12345678");
            }
        };
    }
}
