import axios from 'axios'

const API = axios.create({
    baseURL: 'http://localhost:8080/api',
})

export default {

    listarClientes() {
        return API.get('/clientes')
    },

    crearCliente(cliente) {
        return API.post('/clientes', cliente)
    },

    eliminarCliente(id) {
        return API.delete(`/clientes/${id}`)
    },

    eliminarDireccion(clienteId, direccionId) {
        return API.delete(`/clientes/${clienteId}/direcciones/${direccionId}`)
    },

    actualizarCliente(id, cliente) {
        return API.put(`/clientes/${id}`, cliente)
    }

}
