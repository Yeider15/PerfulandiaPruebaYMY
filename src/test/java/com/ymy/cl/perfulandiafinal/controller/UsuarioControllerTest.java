package com.ymy.cl.perfulandiafinal.controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymy.cl.perfulandiafinal.MariaUsuario.controller.UsuarioController;
import com.ymy.cl.perfulandiafinal.MariaUsuario.model.Usuario;
import com.ymy.cl.perfulandiafinal.MariaUsuario.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(UsuarioController.class)
 public class UsuarioControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UsuarioService usuarioService;

        @Autowired
        private ObjectMapper objectMapper;

        private Usuario usuario;

        @BeforeEach
        void setUp() {
            usuario = new Usuario();
            usuario.setId(1);
            usuario.setNombre("Juan Perez");
            usuario.setCorreo("juan.perez@example.com");
        }

        @Test
        public void testListarUsuarios() throws Exception {
            when(usuarioService.findAll()).thenReturn(List.of(usuario));

            mockMvc.perform(get("/api/v1/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nombre").value("Juan Perez"))
                    .andExpect(jsonPath("$[0].correo").value("juan.perez@example.com"));
        }

        @Test
        public void testObtenerUsuarioPorId() throws Exception {
            when(usuarioService.findById(1)).thenReturn(usuario);

            mockMvc.perform(get("/api/v1/usuarios/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                    .andExpect(jsonPath("$.correo").value("juan.perez@example.com"));
        }

        @Test
        public void testCrearUsuario() throws Exception {
            when(usuarioService.save(any(Usuario.class))).thenReturn("Usuario creado con éxito");

            mockMvc.perform(post("/api/v1/usuarios")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(usuario)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("Usuario creado con éxito"));
        }

        @Test
        public void testEliminarUsuario() throws Exception {
            when(usuarioService.delete(1)).thenReturn("Usuario eliminado con éxito");

            mockMvc.perform(delete("/api/v1/usuarios/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Usuario eliminado con éxito"));

            verify(usuarioService, times(1)).delete(1);
        }

        @Test
        public void testActualizarUsuario() throws Exception {
            usuario.setNombre("Juan Carlos Perez");

            when(usuarioService.save(any(Usuario.class))).thenReturn("Usuario actualizado con éxito");

            mockMvc.perform(put("/api/v1/usuarios/1")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(usuario)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Usuario actualizado con éxito"));
        }
    }



