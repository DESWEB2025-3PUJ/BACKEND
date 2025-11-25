package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de usuarios dentro de la empresa
 * HU-02: Registro de usuario en empresa
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            EmpresaRepository empresaRepository,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // ============ Métodos Legacy (mantener compatibilidad) ============
    
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found with id " + id));
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario not found with id " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // ============ Nuevos métodos para HU-02 ============

    /**
     * HU-02: Crear usuario en empresa (solo ADMINISTRADOR)
     * Como administrador de empresa, quiero crear cuentas de usuario
     */
    @Transactional
    @Secured("ROLE_ADMINISTRADOR")
    public UsuarioDto crearUsuario(UsuarioDto usuarioDto, Long empresaId) {
        // Validar que el correo no exista
        if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Buscar la empresa
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // Crear el usuario
        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        usuario.setEmpresa(empresa);
        
        // HU-02: Validar y asignar rol (ADMINISTRADOR, EDITOR, SOLO_LECTURA)
        if (usuarioDto.getRol() == null || usuarioDto.getRol().isEmpty()) {
            usuario.setRol("EDITOR");  // Rol por defecto
        } else {
            validarRol(usuarioDto.getRol());
            usuario.setRol(usuarioDto.getRol());
        }

        usuario = usuarioRepository.save(usuario);

        // Preparar respuesta sin contraseña
        UsuarioDto responseDto = modelMapper.map(usuario, UsuarioDto.class);
        responseDto.setEmpresaId(empresa.getId());
        responseDto.setPassword(null);

        return responseDto;
    }

    /**
     * HU-02: Obtener todos los usuarios de una empresa
     */
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR", "ROLE_SOLO_LECTURA"})
    public List<UsuarioDto> obtenerUsuariosPorEmpresa(Long empresaId) {
        List<Usuario> usuarios = usuarioRepository.findByEmpresaId(empresaId);
        
        return usuarios.stream()
                .map(usuario -> {
                    UsuarioDto dto = modelMapper.map(usuario, UsuarioDto.class);
                    dto.setEmpresaId(empresaId);
                    dto.setPassword(null);  // No devolver contraseña
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * HU-02: Actualizar usuario (solo ADMINISTRADOR)
     */
    @Transactional
    @Secured("ROLE_ADMINISTRADOR")
    public UsuarioDto actualizarUsuario(Long usuarioId, UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar campos permitidos
        if (usuarioDto.getNombre() != null) {
            usuario.setNombre(usuarioDto.getNombre());
        }
        
        // HU-02: Actualizar rol si se proporciona
        if (usuarioDto.getRol() != null && !usuarioDto.getRol().isEmpty()) {
            validarRol(usuarioDto.getRol());
            usuario.setRol(usuarioDto.getRol());
        }

        // Actualizar contraseña solo si se proporciona una nueva
        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        }

        usuario = usuarioRepository.save(usuario);

        UsuarioDto responseDto = modelMapper.map(usuario, UsuarioDto.class);
        responseDto.setEmpresaId(usuario.getEmpresa().getId());
        responseDto.setPassword(null);

        return responseDto;
    }

    /**
     * HU-02: Eliminar usuario (solo ADMINISTRADOR)
     */
    @Transactional
    @Secured("ROLE_ADMINISTRADOR")
    public void eliminarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que no sea el último administrador
        long countAdmins = usuarioRepository.countByEmpresaIdAndRol(
                usuario.getEmpresa().getId(), "ADMINISTRADOR");
        
        if ("ADMINISTRADOR".equals(usuario.getRol()) && countAdmins <= 1) {
            throw new RuntimeException("No se puede eliminar el último administrador de la empresa");
        }

        usuarioRepository.delete(usuario);
    }

    /**
     * HU-02: Validar que el rol sea válido
     */
    private void validarRol(String rol) {
        if (!"ADMINISTRADOR".equals(rol) && 
            !"EDITOR".equals(rol) && 
            !"SOLO_LECTURA".equals(rol)) {
            throw new RuntimeException("Rol inválido. Roles permitidos: ADMINISTRADOR, EDITOR, SOLO_LECTURA");
        }
    }

    /**
     * Obtener usuario por ID
     */
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR", "ROLE_SOLO_LECTURA"})
    public UsuarioDto obtenerUsuarioPorId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioDto dto = modelMapper.map(usuario, UsuarioDto.class);
        dto.setEmpresaId(usuario.getEmpresa().getId());
        dto.setPassword(null);

        return dto;
    }
}
