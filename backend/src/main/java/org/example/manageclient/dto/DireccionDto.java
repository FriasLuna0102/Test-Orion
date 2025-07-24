package org.example.manageclient.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.manageclient.entity.Direccion;

@Setter
@Getter
public class DireccionDto {

    public Long id;

    @NotBlank(message = "La calle no puede estar vacía")
    public String calle;

    @NotBlank(message = "La ciudad no puede estar vacía")
    public String ciudad;

    @NotBlank(message = "El país no puede estar vacío")
    public String pais;


    public static DireccionDto fromEntity(Direccion d) {
        DireccionDto dto = new DireccionDto();
        dto.setId(d.getId());
        dto.setCalle(d.getCalle());
        dto.setCiudad(d.getCiudad());
        dto.setPais(d.getPais());
        return dto;
    }

}
