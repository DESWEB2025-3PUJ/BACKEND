package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.*;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import com.wikigroup.desarrolloweb.security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de autenticación que maneja el registro y login de usuarios.
 * 
 * Responsabilidades:
 * - Signup (registro): Crea empresa y usuario administrador
 * - Login (inicio de sesión): Valida credenciales y genera JWT
 * - Integración con Spring Security AuthenticationManager
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository, 
            EmpresaRepository empresaRepository, 
            JwtService jwtService,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registro de nuevo usuario (Signup) - HU-01: Registro de empresa
     * - Crea una nueva empresa con NIT y correo de contacto
     * - Crea un usuario administrador para la empresa
     * - Encripta la contraseña con BCrypt
     * - Genera y devuelve un JWT
     */
    @Transactional
    public AuthResponse signup(RegisterRequest request) {
        // HU-02: Validar que el correo del usuario no exista
        if (usuarioRepository.existsByEmail(request.getUsuario().getEmail())) {
            throw new RuntimeException("El correo del usuario ya está registrado");
        }

        // HU-01: Validar que el NIT de la empresa no exista
        if (request.getEmpresa().getNit() != null && 
            empresaRepository.existsByNit(request.getEmpresa().getNit())) {
            throw new RuntimeException("El NIT de la empresa ya está registrado");
        }

        // HU-01: Crear la empresa con NIT y correo de contacto
        Empresa empresa = modelMapper.map(request.getEmpresa(), Empresa.class);
        empresa = empresaRepository.save(empresa);

        // HU-01: Crear el usuario administrador inicial
        Usuario usuario = modelMapper.map(request.getUsuario(), Usuario.class);
        usuario.setPassword(passwordEncoder.encode(request.getUsuario().getPassword()));
        usuario.setRol("ADMINISTRADOR");  // HU-01: Se genera un usuario administrador inicial
        usuario.setEmpresa(empresa);
        usuario = usuarioRepository.save(usuario);

        // Generar token JWT
        String token = jwtService.generateToken(
            usuario.getEmail(), 
            usuario.getEmpresa().getId(), 
            usuario.getRol()
        );

        // Preparar respuesta
        UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
        usuarioDto.setEmpresaId(empresa.getId());
        usuarioDto.setPassword(null); // No devolver la contraseña

        return new AuthResponse(token, usuarioDto);
    }

    /**
     * Registro de nuevo usuario (alias de signup para compatibilidad)
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        return signup(request);
    }

    /**
     * Inicio de sesión (Login)
     * - Usa AuthenticationManager para validar credenciales
     * - Si son correctas, busca el usuario en BD
     * - Genera y devuelve un JWT
     */
    public AuthResponse login(LoginRequest request) {
        // Autenticar con Spring Security
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getCorreo(),
                request.getPassword()
            )
        );

        // Si llegamos aquí, las credenciales son válidas
        // Buscar usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(request.getCorreo())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token JWT
        String token = jwtService.generateToken(
            usuario.getEmail(), 
            usuario.getEmpresa().getId(), 
            usuario.getRol()
        );

        // Preparar respuesta
        UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
        usuarioDto.setEmpresaId(usuario.getEmpresa().getId());
        usuarioDto.setPassword(null); // No devolver la contraseña

        return new AuthResponse(token, usuarioDto);
    }
}
