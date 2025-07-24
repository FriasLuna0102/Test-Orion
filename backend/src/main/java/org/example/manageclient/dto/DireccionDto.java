package org.example.manageclient.dto;

import jakarta.validation.constraints.NotBlank;

public class DireccionDto {

    public Long id;

    @NotBlank(message = "La calle no puede estar vacía")
    public String calle;

    @NotBlank(message = "La ciudad no puede estar vacía")
    public String ciudad;

    @NotBlank(message = "El país no puede estar vacío")
    public String pais;
}
