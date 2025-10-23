package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.model.Empresa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testSave() {
        Empresa empresa = new Empresa();
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
        entityManager.persistAndFlush(empresa);

        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        assertThat(savedUsuario.getId()).isNotNull();
        assertThat(savedUsuario.getNombre()).isEqualTo("Test User");
        assertThat(savedUsuario.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindById() {
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

        Optional<Usuario> foundUsuario = usuarioRepository.findById(usuario.getId());

        assertThat(foundUsuario).isPresent();
        assertThat(foundUsuario.get().getNombre()).isEqualTo("Test User");
    }

    @Test
    void testFindAll() {
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

        List<Usuario> usuarios = usuarioRepository.findAll();

        assertThat(usuarios).hasSize(2);
    }

    @Test
    void testDeleteById() {
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

        usuarioRepository.deleteById(usuarioId);
        entityManager.flush();

        Optional<Usuario> deletedUsuario = usuarioRepository.findById(usuarioId);
        assertThat(deletedUsuario).isEmpty();
    }

    @Test
    void testExistsById() {
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

        assertThat(usuarioRepository.existsById(usuario.getId())).isTrue();
        assertThat(usuarioRepository.existsById(999L)).isFalse();
    }
}
