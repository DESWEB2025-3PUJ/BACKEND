package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.AuthResponse;
import com.wikigroup.desarrolloweb.dtos.LoginRequest;
import com.wikigroup.desarrolloweb.dtos.RegisterRequest;
import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;
import com.wikigroup.desarrolloweb.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository, JwtUtil jwtUtil,ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        // 1) Validaciones
        if (usuarioRepository.existsByEmail(request.getUsuario().getEmail())) {
            // Mejor si tienes una BadRequestException propia mapeada por @ControllerAdvice
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        // 2) Crear empresa
        Empresa empresa = mapper.map(request.getEmpresa(), Empresa.class);
        empresa = empresaRepository.save(empresa);
        // 3) Crear usuario ADMINISTRADOR
        Usuario usuario = mapper.map(request.getUsuario(), Usuario.class);
        usuario.setPassword(passwordEncoder.encode(request.getUsuario().getPassword()));
        usuario.setRol("ADMINISTRADOR");
        usuario.setEmpresa(empresa);
        usuario = usuarioRepository.save(usuario);
        // 4) Generar token
        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                empresa.getId(),
                usuario.getRol()
        );
        // 5) Respuesta (sanitizar password)
        UsuarioDto usuarioDto = mapper.map(usuario, UsuarioDto.class);
        usuarioDto.setEmpresaId(empresa.getId());
        usuarioDto.setPassword(null);

        return new AuthResponse(token, usuarioDto);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // 1) Buscar usuario
        Usuario usuario = usuarioRepository.findByEmail(request.getCorreo())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        // 2) Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            // Si tienes UnauthorizedException, úsala y mapea a 401 en tu @ControllerAdvice
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        // 3) Generar token
        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getEmpresa().getId(),
                usuario.getRol()
        );
        // 4) Respuesta (sanitizar password)
        UsuarioDto usuarioDto = mapper.map(usuario, UsuarioDto.class);
        usuarioDto.setEmpresaId(usuario.getEmpresa().getId());
        usuarioDto.setPassword(null);

        return new AuthResponse(token, usuarioDto);
    }
}
