package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Empresa empresa;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
        empresa = entityManager.persistAndFlush(empresa);

        usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);
    }

    @Test
    void save_ShouldPersistUsuario() {
        // When
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Then
        assertNotNull(savedUsuario.getId());
        assertEquals("Test User", savedUsuario.getNombre());
        assertEquals("test@example.com", savedUsuario.getEmail());
        assertEquals("password123", savedUsuario.getPassword());
        assertEquals(empresa.getId(), savedUsuario.getEmpresa().getId());
    }

    @Test
    void findById_WhenUsuarioExists_ShouldReturnUsuario() {
        // Given
        Usuario savedUsuario = entityManager.persistAndFlush(usuario);

        // When
        Optional<Usuario> found = usuarioRepository.findById(savedUsuario.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getNombre());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void findById_WhenUsuarioDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<Usuario> found = usuarioRepository.findById(999L);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllUsuarios() {
        // Given
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Test User 2");
        usuario2.setEmail("test2@example.com");
        usuario2.setPassword("password456");
        usuario2.setEmpresa(empresa);

        entityManager.persistAndFlush(usuario);
        entityManager.persistAndFlush(usuario2);

        // When
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Then
        assertEquals(2, usuarios.size());
        assertTrue(usuarios.stream().anyMatch(u -> u.getNombre().equals("Test User")));
        assertTrue(usuarios.stream().anyMatch(u -> u.getNombre().equals("Test User 2")));
    }

    @Test
    void deleteById_ShouldRemoveUsuario() {
        // Given
        Usuario savedUsuario = entityManager.persistAndFlush(usuario);
        Long usuarioId = savedUsuario.getId();

        // When
        usuarioRepository.deleteById(usuarioId);
        entityManager.flush();

        // Then
        Optional<Usuario> found = usuarioRepository.findById(usuarioId);
        assertFalse(found.isPresent());
    }

    @Test
    void existsById_WhenUsuarioExists_ShouldReturnTrue() {
        // Given
        Usuario savedUsuario = entityManager.persistAndFlush(usuario);

        // When
        boolean exists = usuarioRepository.existsById(savedUsuario.getId());

        // Then
        assertTrue(exists);
    }

    @Test
    void existsById_WhenUsuarioDoesNotExist_ShouldReturnFalse() {
        // When
        boolean exists = usuarioRepository.existsById(999L);

        // Then
        assertFalse(exists);
    }
}