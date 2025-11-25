package com.wikigroup.desarrolloweb.security;

import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que carga los detalles del usuario desde la base de datos.
 * Spring Security lo usa para autenticar y autorizar usuarios.
 * 
 * Roles del sistema (HU-02):
 * - ADMINISTRADOR: Acceso total, puede gestionar usuarios, procesos, actividades, gateways y roles
 * - EDITOR: Puede crear y editar procesos, actividades, arcos y gateways
 * - SOLO_LECTURA: Solo puede consultar y visualizar procesos
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar usuario por email (HU-03: Inicio de sesión)
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Convertir el rol de usuario en una autoridad de Spring Security
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRol() != null) {
            // Spring Security requiere el prefijo ROLE_
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));
        }

        // Retornar UserDetails de Spring Security
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
