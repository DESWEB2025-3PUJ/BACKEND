package com.wikigroup.desarrolloweb.integration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.wikigroup.desarrolloweb.dtos.LoginRequest;
import com.wikigroup.desarrolloweb.dtos.RegisterRequest;
import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") 
@AutoConfigureTestDatabase
class AuthAndEmpresaIntegrationTest {

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
        empresaDto.setNombre("Empresa Test");
        empresaDto.setNit("900123456");
        empresaDto.setCorreoContacto("contacto@empresatest.com");

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setNombre("Admin Test");
        usuarioDto.setEmail("admin@empresatest.com");
        usuarioDto.setPassword("password123");
        usuarioDto.setRol("ADMINISTRADOR");

        RegisterRequest request = new RegisterRequest();
        request.setEmpresa(empresaDto);
        request.setUsuario(usuarioDto);

        return request;
    }

    @Test
    void signupYLogin_DebenPersistirUsuarioYDevolverJwt() {
        // 1. Signup
        RegisterRequest registerRequest = buildRegisterRequest();

        ResponseEntity<Map> signupResponse =
                restTemplate.postForEntity("/api/auth/signup", registerRequest, Map.class);

        assertEquals(HttpStatus.OK, signupResponse.getStatusCode(), "Signup debería devolver 200 OK");
        Map<String, Object> body = signupResponse.getBody();
        assertNotNull(body, "El body de la respuesta no debería ser nulo");

        String tokenSignup = (String) body.get("token");
        assertNotNull(tokenSignup, "El token no debería ser nulo");
        assertFalse(tokenSignup.isBlank(), "El token no debería estar vacío");

        Map usuarioMap = (Map) body.get("usuario");
        assertNotNull(usuarioMap, "El objeto usuario no debería ser nulo");
        assertEquals("admin@empresatest.com", usuarioMap.get("email"));

        assertTrue(
                usuarioRepository.findByEmail("admin@empresatest.com").isPresent(),
                "El usuario debería existir en la base de datos"
        );

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCorreo("admin@empresatest.com");
        loginRequest.setPassword("password123");

        ResponseEntity<Map> loginResponse =
                restTemplate.postForEntity("/api/auth/login", loginRequest, Map.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "Login debería devolver 200 OK");
        Map<String, Object> loginBody = loginResponse.getBody();
        assertNotNull(loginBody);
        String tokenLogin = (String) loginBody.get("token");
        assertNotNull(tokenLogin, "El token de login no debería ser nulo");
        assertFalse(tokenLogin.isBlank(), "El token de login no debería estar vacío");
    }

    @Test
    void loginConPasswordIncorrecta_DebeDevolver401() {
        RegisterRequest registerRequest = buildRegisterRequest();
        restTemplate.postForEntity("/api/auth/signup", registerRequest, Map.class);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCorreo("admin@empresatest.com");
        loginRequest.setPassword("password-INVALIDA");

        ResponseEntity<String> response =
                restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Login con contraseña incorrecta debe devolver 401 UNAUTHORIZED");
    }

    @Test
    void accederEndpointProtegidoSinToken_DebeFallar() {
    
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa X");
        empresa.setNit("123456");
        empresa.setCorreoContacto("contacto@x.com");
        empresa = empresaRepository.save(empresa);

        ResponseEntity<String> response =
                restTemplate.getForEntity("/api/empresas/" + empresa.getId(), String.class);


        assertTrue(
                response.getStatusCode() == HttpStatus.UNAUTHORIZED
                        || response.getStatusCode() == HttpStatus.FORBIDDEN,
                "Sin token debería devolver 401 o 403, pero devolvió: " + response.getStatusCode()
        );
    }

    @Test
    void accederEndpointProtegidoConJwtValido_DebeFuncionar() {

        RegisterRequest registerRequest = buildRegisterRequest();

        ResponseEntity<Map> signupResponse =
                restTemplate.postForEntity("/api/auth/signup", registerRequest, Map.class);

        assertEquals(HttpStatus.OK, signupResponse.getStatusCode());
        Map<String, Object> body = signupResponse.getBody();
        assertNotNull(body);
        String token = (String) body.get("token");
        assertNotNull(token);

        Empresa empresa = empresaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró empresa creada"));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<EmpresaDto> response =
                restTemplate.exchange(
                        "/api/empresas/" + empresa.getId(),
                        HttpMethod.GET,
                        entity,
                        EmpresaDto.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Con JWT válido debe devolver 200");
        assertNotNull(response.getBody(), "El body no debería ser nulo");
        assertEquals(empresa.getId(), response.getBody().getId(), "El id de empresa debe coincidir");
    }
}
       