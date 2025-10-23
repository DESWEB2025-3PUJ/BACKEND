package com.wikigroup.desarrolloweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.service.UsuarioService;
import com.wikigroup.desarrolloweb.shared.ApiExceptionHandler;
import com.wikigroup.desarrolloweb.shared.BadRequestException;
import com.wikigroup.desarrolloweb.shared.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@Import(ApiExceptionHandler.class)
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
        when(usuarioService.getAll()).thenReturn(List.of(usuarioDto));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(usuarioService, times(1)).getAll();
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void getById_WhenUsuarioExists_ShouldReturnUsuario() throws Exception {
        when(usuarioService.getById(1L)).thenReturn(usuarioDto);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(usuarioService, times(1)).getById(1L);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void getById_WhenNotFound_ShouldReturn404() throws Exception {
        when(usuarioService.getById(99L)).thenThrow(new NotFoundException("Usuario not found"));

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).getById(99L);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void create_ShouldCreateAndReturnUsuario() throws Exception {
        when(usuarioService.create(any(UsuarioDto.class))).thenReturn(usuarioDto);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/usuarios/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(usuarioService, times(1)).create(any(UsuarioDto.class));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void create_WhenBadRequest_ShouldReturn400() throws Exception {
        when(usuarioService.create(any(UsuarioDto.class)))
                .thenThrow(new BadRequestException("Correo ya registrado"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, times(1)).create(any(UsuarioDto.class));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void update_ShouldUpdateAndReturnUsuario() throws Exception {
        when(usuarioService.update(eq(1L), any(UsuarioDto.class))).thenReturn(usuarioDto);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test User"));

        verify(usuarioService, times(1)).update(eq(1L), any(UsuarioDto.class));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void delete_ShouldDeleteUsuario() throws Exception {
        doNothing().when(usuarioService).delete(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).delete(1L);
        verifyNoMoreInteractions(usuarioService);
    }
}
