package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.*;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import com.wikigroup.desarrolloweb.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, 
                      EmpresaRepository empresaRepository, 
                      JwtUtil jwtUtil,
                      ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar que el correo no exista
        if (usuarioRepository.existsByEmail(request.getUsuario().getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Crear la empresa
        Empresa empresa = modelMapper.map(request.getEmpresa(), Empresa.class);
        empresa = empresaRepository.save(empresa);

        // Crear el usuario con rol ADMINISTRADOR
        Usuario usuario = modelMapper.map(request.getUsuario(), Usuario.class);
        usuario.setPassword(passwordEncoder.encode(request.getUsuario().getPassword()));
        usuario.setRol("ADMINISTRADOR");
        usuario.setEmpresa(empresa);
        usuario = usuarioRepository.save(usuario);

        // Generar token
        String token = jwtUtil.generateToken(
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

    public AuthResponse login(LoginRequest request) {
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByEmail(request.getCorreo())
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Generar token
        String token = jwtUtil.generateToken(
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
