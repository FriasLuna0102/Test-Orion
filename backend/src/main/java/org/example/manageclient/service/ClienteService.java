package org.example.manageclient.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.manageclient.dto.ClienteDto;
import org.example.manageclient.dto.DireccionDto;
import org.example.manageclient.entity.Cliente;
import org.example.manageclient.entity.Direccion;
import org.example.manageclient.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteDto> listarClientes() {
        return clienteRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ClienteDto obtenerCliente(Long id) {
        return clienteRepository.findById(id).map(this::toDto).orElse(null);
    }

    public ClienteDto crearCliente(ClienteDto dto) {
        Cliente cliente = toEntity(dto);
        cliente.getDirecciones().forEach(d -> d.setCliente(cliente));
        Cliente guardado = clienteRepository.save(cliente);
        return toDto(guardado);
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public ClienteDto actualizarCliente(Long id, ClienteDto dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        cliente.setNombre(dto.getNombre());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());

        cliente.getDirecciones().clear();
        for (DireccionDto dirDto : dto.getDirecciones()) {
            Direccion dir = new Direccion();
            dir.setCalle(dirDto.getCalle());
            dir.setCiudad(dirDto.getCiudad());
            dir.setPais(dirDto.getPais());
            dir.setCliente(cliente);
            cliente.getDirecciones().add(dir);
        }

        Cliente actualizado = clienteRepository.save(cliente);
        return ClienteDto.fromEntity(actualizado);
    }


    public void eliminarDireccion(Long clienteId, Long direccionId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        cliente.getDirecciones().removeIf(d -> d.getId().equals(direccionId));
        clienteRepository.save(cliente);
    }



    private ClienteDto toDto(Cliente c) {
        ClienteDto dto = new ClienteDto();
        dto.id = c.getId();
        dto.nombre = c.getNombre();
        dto.telefono = c.getTelefono();
        dto.email = c.getEmail();
        dto.direcciones = c.getDirecciones().stream().map(d -> {
            DireccionDto dd = new DireccionDto();
            dd.id = d.getId();
            dd.calle = d.getCalle();
            dd.ciudad = d.getCiudad();
            dd.pais = d.getPais();
            return dd;
        }).collect(Collectors.toList());
        return dto;
    }

    private Cliente toEntity(ClienteDto dto) {
        Cliente c = new Cliente();
        c.setId(dto.id);
        c.setNombre(dto.nombre);
        c.setTelefono(dto.telefono);
        c.setEmail(dto.email);
        c.setDirecciones(dto.direcciones.stream().map(d -> {
            Direccion dir = new Direccion();
            dir.setId(d.id);
            dir.setCalle(d.calle);
            dir.setCiudad(d.ciudad);
            dir.setPais(d.pais);
            return dir;
        }).collect(Collectors.toList()));
        return c;
    }
}
