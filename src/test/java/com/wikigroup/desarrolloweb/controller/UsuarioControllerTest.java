package com.wikigroup.desarrolloweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.service.EmpresaService;
import com.wikigroup.desarrolloweb.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EmpresaService empresaService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;
    private UsuarioDto usuarioDto;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Test Company");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);

        usuarioDto = new UsuarioDto();
        usuarioDto.setId(1L);
        usuarioDto.setNombre("Test User");
        usuarioDto.setEmail("test@example.com");
        usuarioDto.setEmpresaId(1L);
    }

    @Test
    void getAll_ShouldReturnListOfUsuarios() throws Exception {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario);
        List<UsuarioDto> usuarioDtos = Arrays.asList(usuarioDto);
        
        when(usuarioService.findAll()).thenReturn(usuarios);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDto.class))).thenReturn(usuarioDto);

        // When & Then
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void getById_WhenUsuarioExists_ShouldReturnUsuario() throws Exception {
        // Given
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioDto.class)).thenReturn(usuarioDto);

        // When & Then
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    void create_ShouldCreateAndReturnUsuario() throws Exception {
        // Given
        when(empresaService.findById(1L)).thenReturn(empresa);
        when(modelMapper.map(any(UsuarioDto.class), eq(Usuario.class))).thenReturn(usuario);
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDto.class))).thenReturn(usuarioDto);

        // When & Then
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(empresaService, times(1)).findById(1L);
        verify(usuarioService, times(1)).save(any(Usuario.class));
    }

    @Test
    void update_ShouldUpdateAndReturnUsuario() throws Exception {
        // Given
        when(empresaService.findById(1L)).thenReturn(empresa);
        when(modelMapper.map(any(UsuarioDto.class), eq(Usuario.class))).thenReturn(usuario);
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioDto.class))).thenReturn(usuarioDto);

        // When & Then
        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test User"));

        verify(empresaService, times(1)).findById(1L);
        verify(usuarioService, times(1)).save(any(Usuario.class));
    }

    @Test
    void delete_ShouldDeleteUsuario() throws Exception {
        // Given
        doNothing().when(usuarioService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).delete(1L);
    }
}