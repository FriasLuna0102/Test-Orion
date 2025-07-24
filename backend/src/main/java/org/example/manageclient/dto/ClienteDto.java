package org.example.manageclient.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.manageclient.entity.Cliente;
import org.springframework.beans.factory.annotation.Lookup;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ClienteDto {
    public Long id;

    @NotBlank(message = "El nombre del cliente no puede estar en blanco")
    public String nombre;
    @NotBlank(message = "El apellido del cliente no puede estar en blanco")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    public String telefono;
    @NotBlank(message = "El email del cliente no puede estar en blanco")
    public String email;

    @NotEmpty(message = "Debe proporcionar al menos una dirección")
    @Valid
    public List<DireccionDto> direcciones;

    public static ClienteDto fromEntity(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setDirecciones(
                cliente.getDirecciones()
                        .stream()
                        .map(DireccionDto::fromEntity)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}


