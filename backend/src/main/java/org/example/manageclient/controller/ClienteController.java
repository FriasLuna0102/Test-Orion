package org.example.manageclient.controller;

import jakarta.validation.Valid;
import org.example.manageclient.dto.ClienteDto;
import org.example.manageclient.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteDto> listar() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public ClienteDto obtener(@PathVariable Long id) {
        return clienteService.obtenerCliente(id);
    }

    @PostMapping
    public ClienteDto crear(@Valid @RequestBody ClienteDto dto) {
        return clienteService.crearCliente(dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }
}
