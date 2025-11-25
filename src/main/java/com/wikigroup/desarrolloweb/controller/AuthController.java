package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.AuthResponse;
import com.wikigroup.desarrolloweb.dtos.LoginRequest;
import com.wikigroup.desarrolloweb.dtos.RegisterRequest;
import com.wikigroup.desarrolloweb.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación - Endpoints públicos.
 * 
 * Endpoints:
 * - POST /api/auth/signup  -> Registro de nuevos usuarios
 * - POST /api/auth/login   -> Inicio de sesión
 * - POST /api/auth/register -> Alias de signup (compatibilidad)
 * 
 * Ambos devuelven un AuthResponse con el token JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de registro (signup)
     * Crea una nueva empresa y un usuario administrador
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.signup(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint de registro (register) - Alias para compatibilidad
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint de login
     * Valida credenciales y retorna JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Credenciales inválidas"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Clase interna para respuestas de error
     */
    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
