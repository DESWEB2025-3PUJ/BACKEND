package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.config.TestSecurityConfig;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;                  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;            
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.wikigroup.desarrolloweb.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;     

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
@Import(TestSecurityConfig.class)
public class UsuarioControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void setUp() {
        // Limpiar datos de prueba
        usuarioRepository.deleteAll();
        empresaRepository.deleteAll();

        // Crear empresa real en BD
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa Test");
        empresa = empresaRepository.save(empresa);

        // Crear usuario real en BD
        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);

        usuarioRepository.save(usuario);
    }

    @Test
    void getAll_DevuelveUsuariosDesdeBDReal() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }
}
