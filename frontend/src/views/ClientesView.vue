<template>
  <div class="p-8 max-w-5xl mx-auto text-white">
    <h1 class="text-3xl font-bold text-center mb-8 text-white">📋 Gestión de Clientes</h1>

    <!-- FORMULARIO DE CLIENTE -->
    <form @submit.prevent="crearCliente" class="bg-gray-800 p-6 rounded-lg shadow-lg space-y-4 mb-10">
      <div class="space-y-2">
        <input v-model="nuevo.nombre" placeholder="Nombre" class="input" />
        <input v-model="nuevo.telefono" placeholder="Teléfono" class="input" />
        <input v-model="nuevo.email" placeholder="Email" class="input" />
      </div>

      <div class="mt-4">
        <h2 class="font-semibold text-lg mb-2 text-white">📍 Direcciones</h2>
        <div
            v-for="(dir, index) in nuevo.direcciones"
            :key="index"
            class="bg-gray-700 p-4 pr-8 rounded mb-2 space-y-2 relative"
        >
          <div class="grid grid-cols-3 gap-2">
            <input v-model="dir.calle" placeholder="Calle" class="input" />
            <input v-model="dir.ciudad" placeholder="Ciudad" class="input" />
            <input v-model="dir.pais" placeholder="País" class="input" />
          </div>

          <button
              v-if="dir.id"
              type="button"
              @click="eliminarDireccionBackend(dir.id, index)"
              class="absolute top-2 right-2 text-red-400 hover:text-red-600 text-xl"
              title="Eliminar dirección"
          >
            ✕
          </button>

          <button
              v-else
              type="button"
              @click="eliminarDireccionLocal(index)"
              class="absolute top-2 right-2 text-red-400 hover:text-red-600 text-xl"
              title="Eliminar dirección nueva"
          >
            ✕
          </button>
        </div>

        <button
            type="button"
            @click="agregarDireccion"
            class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded shadow"
        >
          + Agregar Dirección
        </button>
      </div>

      <div v-if="error" class="bg-red-700 text-white p-3 rounded text-sm">
        {{ error }}
      </div>

      <button
          type="submit"
          class="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-2 rounded-lg transition"
      >
        Guardar Cliente
      </button>
    </form>

    <!-- LISTADO DE CLIENTES -->
    <div class="bg-gray-900 p-6 rounded shadow-lg">
      <h2 class="text-xl font-bold mb-4">📦 Clientes registrados</h2>
      <ul v-if="clientes.length" class="space-y-4">
        <li v-for="cliente in clientes" :key="cliente.id" class="border-b border-gray-700 pb-3">
          <div class="mb-1 font-semibold">{{ cliente.nombre }}</div>
          <div class="text-sm text-gray-300">
            📧 {{ cliente.email }} — 📞 {{ cliente.telefono }}
          </div>
          <ul class="mt-1 ml-4 text-sm text-gray-400 list-disc">
            <li v-for="(dir, i) in cliente.direcciones" :key="i">
              🏠 {{ dir.calle }}, {{ dir.ciudad }}, {{ dir.pais }}
            </li>
          </ul>

          <button
              @click="eliminarCliente(cliente.id)"
              class="text-red-500 hover:text-red-700 text-sm ml-2"
          >
            🗑️ Eliminar
          </button>

          <button
              @click="editarCliente(cliente)"
              class="text-blue-500 hover:text-blue-700 text-sm ml-2"
          >
            ✏️ Editar
          </button>

        </li>
      </ul>
      <p v-else>No hay clientes registrados aún.</p>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import api from '../services/api.js'

const clientes = ref([])
const error = ref('')

const nuevo = ref({
  id: null,
  nombre: '',
  telefono: '',
  email: '',
  direcciones: [{calle: '', ciudad: '', pais: ''}]
})

const cargar = async () => {
  try {
    const res = await api.listarClientes()
    clientes.value = res.data
  } catch (e) {
    error.value = 'Error al cargar los clientes.'
  }
}

const crearCliente = async () => {
  error.value = ''
  try {
    if (nuevo.value.id) {
      await api.actualizarCliente(nuevo.value.id, nuevo.value)
    } else {
      await api.crearCliente(nuevo.value)
    }

    nuevo.value = {
      id: null,
      nombre: '',
      telefono: '',
      email: '',
      direcciones: [{calle: '', ciudad: '', pais: ''}]
    }

    await cargar()
  } catch (e) {
    if (e.response?.data?.message) {
      error.value = e.response.data.message
    } else {
      error.value = 'Error al guardar el cliente.'
    }
  }
}


const agregarDireccion = () => {
  nuevo.value.direcciones.push({calle: '', ciudad: '', pais: ''})
}

const eliminarCliente = async (id) => {
  try {
    await api.eliminarCliente(id)
    await cargar()
  } catch (e) {
    error.value = 'Error al eliminar cliente.'
  }
}

const editarCliente = (cliente) => {
  nuevo.value = {
    id: cliente.id,
    nombre: cliente.nombre,
    telefono: cliente.telefono,
    email: cliente.email,
    direcciones: cliente.direcciones.map(d => ({...d}))
  }
}


const eliminarDireccionLocal = (index) => {
  nuevo.value.direcciones.splice(index, 1)
}

const eliminarDireccionBackend = async (direccionId, index) => {
  if (!nuevo.value.id) return
  try {
    await api.eliminarDireccion(nuevo.value.id, direccionId)
    nuevo.value.direcciones.splice(index, 1)
    await cargar()
  } catch (e) {
    error.value = 'Error al eliminar la dirección.'
  }
}

onMounted(cargar)
</script>

<style scoped>
.input {
  @apply bg-gray-900 text-white border border-gray-600 px-3 py-2 rounded w-full focus:outline-none focus:ring-2 focus:ring-blue-500;
}
</style>
