package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.model.Empresa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testSave() {
        // Given
        Empresa empresa = new Empresa();
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
        entityManager.persistAndFlush(empresa);

        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);

        // When
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Then
        assertThat(savedUsuario.getId()).isNotNull();
        assertThat(savedUsuario.getNombre()).isEqualTo("Test User");
        assertThat(savedUsuario.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindById() {
        // Given
        Empresa empresa = new Empresa();
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
        entityManager.persistAndFlush(empresa);

        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);
        entityManager.persistAndFlush(usuario);

        // When
        Optional<Usuario> foundUsuario = usuarioRepository.findById(usuario.getId());

        // Then
        assertThat(foundUsuario).isPresent();
        assertThat(foundUsuario.get().getNombre()).isEqualTo("Test User");
    }

    @Test
    void testFindAll() {
        // Given
        Empresa empresa = new Empresa();
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
        entityManager.persistAndFlush(empresa);

        Usuario usuario1 = new Usuario();
        usuario1.setNombre("User 1");
        usuario1.setEmail("user1@example.com");
        usuario1.setPassword("password123");
        usuario1.setEmpresa(empresa);
        entityManager.persistAndFlush(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("User 2");
        usuario2.setEmail("user2@example.com");
        usuario2.setPassword("password123");
        usuario2.setEmpresa(empresa);
        entityManager.persistAndFlush(usuario2);

        // When
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Then
        assertThat(usuarios).hasSize(2);
    }

    @Test
    void testDeleteById() {
        // Given
        Empresa empresa = new Empresa();
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
        entityManager.persistAndFlush(empresa);

        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);
        entityManager.persistAndFlush(usuario);

        Long usuarioId = usuario.getId();

        // When
        usuarioRepository.deleteById(usuarioId);
        entityManager.flush();

        // Then
        Optional<Usuario> deletedUsuario = usuarioRepository.findById(usuarioId);
        assertThat(deletedUsuario).isEmpty();
    }

    @Test
    void testExistsById() {
        // Given
        Empresa empresa = new Empresa();
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
        entityManager.persistAndFlush(empresa);

        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);
        entityManager.persistAndFlush(usuario);

        // When & Then
        assertThat(usuarioRepository.existsById(usuario.getId())).isTrue();
        assertThat(usuarioRepository.existsById(999L)).isFalse();
    }
}