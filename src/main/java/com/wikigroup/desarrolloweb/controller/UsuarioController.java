package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.service.UsuarioService;
import com.wikigroup.desarrolloweb.service.EmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controlador para gestión de usuarios
 * HU-02: Registro de usuario en empresa
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // ============ Endpoints Legacy (mantener compatibilidad) ============

    @GetMapping
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR", "ROLE_SOLO_LECTURA"})
    public List<UsuarioDto> getAll() {
        return service.findAll()
                .stream()
                .map(u -> {
                    UsuarioDto dto = mapper.map(u, UsuarioDto.class);
                    dto.setPassword(null);  // No devolver contraseña
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR", "ROLE_SOLO_LECTURA"})
    public UsuarioDto getById(@PathVariable Long id) {
        return service.obtenerUsuarioPorId(id);
    }

    // ============ Nuevos endpoints para HU-02 ============

    /**
     * HU-02: Crear usuario en empresa (solo ADMINISTRADOR)
     * POST /api/usuarios/empresa/{empresaId}
     */
    @PostMapping("/empresa/{empresaId}")
    @Secured("ROLE_ADMINISTRADOR")
    public ResponseEntity<?> crearUsuarioEnEmpresa(
            @PathVariable Long empresaId,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto nuevoUsuario = service.crearUsuario(usuarioDto, empresaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * HU-02: Obtener usuarios de una empresa
     * GET /api/usuarios/empresa/{empresaId}
     */
    @GetMapping("/empresa/{empresaId}")
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR", "ROLE_SOLO_LECTURA"})
    public ResponseEntity<List<UsuarioDto>> obtenerUsuariosPorEmpresa(@PathVariable Long empresaId) {
        List<UsuarioDto> usuarios = service.obtenerUsuariosPorEmpresa(empresaId);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * HU-02: Actualizar usuario (solo ADMINISTRADOR)
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    @Secured("ROLE_ADMINISTRADOR")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto usuarioActualizado = service.actualizarUsuario(id, usuarioDto);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * HU-02: Eliminar usuario (solo ADMINISTRADOR)
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMINISTRADOR")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            service.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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

