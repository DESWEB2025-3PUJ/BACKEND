package com.wikigroup.desarrolloweb.integration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.wikigroup.desarrolloweb.dtos.EmpresaDto;
import com.wikigroup.desarrolloweb.dtos.RegisterRequest;
import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class UsuarioIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void limpiarBaseDatos() {
        usuarioRepository.deleteAll();
        empresaRepository.deleteAll();
    }

    private RegisterRequest buildRegisterRequest() {
        EmpresaDto empresaDto = new EmpresaDto();
        empresaDto.setNombre("Empresa Usuarios");
        empresaDto.setNit("900777888");
        empresaDto.setCorreoContacto("contacto@usuarios.com");

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setNombre("Admin Usuarios");
        usuarioDto.setEmail("admin@usuarios.com");
        usuarioDto.setPassword("password123");
        usuarioDto.setRol("ADMINISTRADOR");

        RegisterRequest request = new RegisterRequest();
        request.setEmpresa(empresaDto);
        request.setUsuario(usuarioDto);

        return request;
    }

    private String registrarEmpresaYObtenerTokenAdmin() {
        RegisterRequest registerRequest = buildRegisterRequest();

        ResponseEntity<Map> signupResponse =
                restTemplate.postForEntity("/api/auth/signup", registerRequest, Map.class);

        assertEquals(HttpStatus.OK, signupResponse.getStatusCode());
        Map<String, Object> body = signupResponse.getBody();
        assertNotNull(body);
        String token = (String) body.get("token");
        assertNotNull(token);
        return token;
    }

    @Test
    void listarUsuariosPorEmpresa_conJwtAdmin_DebeDevolver200YLista() {
        String token = registrarEmpresaYObtenerTokenAdmin();

        Empresa empresa = empresaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró empresa creada"));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UsuarioDto[]> response =
                restTemplate.exchange(
                        "/api/usuarios/empresa/" + empresa.getId(),
                        HttpMethod.GET,
                        entity,
                        UsuarioDto[].class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Con JWT admin debe devolver 200");
        assertNotNull(response.getBody(), "El body no debería ser nulo");
        assertTrue(response.getBody().length >= 1, "Debe haber al menos un usuario (el admin)");

        boolean contieneAdmin = false;
        for (UsuarioDto u : response.getBody()) {
            if ("admin@usuarios.com".equals(u.getEmail())) {
                contieneAdmin = true;
                break;
            }
        }
        assertTrue(contieneAdmin, "La lista de usuarios debe contener al admin recién creado");
    }

    @Test
    void listarUsuariosPorEmpresa_sinToken_DebeFallar() {
        String token = registrarEmpresaYObtenerTokenAdmin(); 

        Empresa empresa = empresaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró empresa creada"));

        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        "/api/usuarios/empresa/" + empresa.getId(),
                        String.class
                );

        assertTrue(
                response.getStatusCode() == HttpStatus.UNAUTHORIZED
                        || response.getStatusCode() == HttpStatus.FORBIDDEN,
                "Sin token debería devolver 401 o 403, pero devolvió: " + response.getStatusCode()
        );
    }
}
