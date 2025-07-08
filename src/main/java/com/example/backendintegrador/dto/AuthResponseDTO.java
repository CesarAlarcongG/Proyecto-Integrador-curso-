package com.example.backendintegrador.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;


}
