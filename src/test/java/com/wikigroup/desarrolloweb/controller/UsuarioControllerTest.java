package com.wikigroup.desarrolloweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDto usuarioDto;

    @BeforeEach
    void setUp() {
        usuarioDto = new UsuarioDto();
        usuarioDto.setId(1L);
        usuarioDto.setNombre("Test User");
        usuarioDto.setEmail("test@example.com");
        usuarioDto.setEmpresaId(1L);
    }

    @Test
    void getAll_ShouldReturnListOfUsuarios() throws Exception {
        // Given
        when(usuarioService.getAll()).thenReturn(List.of(usuarioDto));

        // When & Then
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(usuarioService, times(1)).getAll();
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void getById_WhenUsuarioExists_ShouldReturnUsuario() throws Exception {
        // Given
        when(usuarioService.getById(1L)).thenReturn(usuarioDto);

        // When & Then
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(usuarioService, times(1)).getById(1L);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void create_ShouldCreateAndReturnUsuario() throws Exception {
        // Given
        when(usuarioService.create(any(UsuarioDto.class))).thenReturn(usuarioDto);

        // When & Then
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/usuarios/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(usuarioService, times(1)).create(any(UsuarioDto.class));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void update_ShouldUpdateAndReturnUsuario() throws Exception {
        // Given
        when(usuarioService.update(eq(1L), any(UsuarioDto.class))).thenReturn(usuarioDto);

        // When & Then
        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test User"));

        verify(usuarioService, times(1)).update(eq(1L), any(UsuarioDto.class));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void delete_ShouldDeleteUsuario() throws Exception {
        // Given
        doNothing().when(usuarioService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).delete(1L);
        verifyNoMoreInteractions(usuarioService);
    }
}
