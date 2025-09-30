package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
    }

    @Test
    void findAll_ShouldReturnAllUsuarios() {
        // Given
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Test User 2");
        usuario2.setEmail("test2@example.com");
        
        List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // When
        List<Usuario> result = usuarioService.findAll();

        // Then
        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).getNombre());
        assertEquals("Test User 2", result.get(1).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenUsuarioExists_ShouldReturnUsuario() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        Usuario result = usuarioService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getNombre());
        assertEquals("test@example.com", result.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenUsuarioDoesNotExist_ShouldThrowException() {
        // Given
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> usuarioService.findById(1L));
        
        assertEquals("Usuario not found with id 1", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedUsuario() {
        // Given
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario result = usuarioService.save(usuario);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getNombre());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void delete_WhenUsuarioExists_ShouldDeleteUsuario() {
        // Given
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // When
        usuarioService.delete(1L);

        // Then
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_WhenUsuarioDoesNotExist_ShouldThrowException() {
        // Given
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> usuarioService.delete(1L));
        
        assertEquals("Usuario not found with id 1", exception.getMessage());
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}