package com.wikigroup.desarrolloweb.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta todas las peticiones HTTP para validar tokens JWT.
 * 
 * Flujo de autenticación:
 * 1. Extrae el header Authorization de la petición
 * 2. Valida que tenga formato Bearer <token>
 * 3. Verifica que el token no haya expirado
 * 4. Extrae el username (email) del token
 * 5. Carga el usuario de la base de datos
 * 6. Establece la autenticación en el SecurityContextHolder
 * 7. Permite que la petición continúe
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        
        // Si no hay header Authorization o no empieza con "Bearer ", continuar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extraer el token (eliminar "Bearer " del inicio)
            final String jwt = authHeader.substring(7);
            
            // Extraer el email del usuario del token
            final String userEmail = jwtService.extractUsername(jwt);

            // Si hay email y no hay autenticación previa en el contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Cargar los detalles del usuario desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Validar el token
                if (jwtService.isTokenValid(jwt)) {
                    
                    // Crear el token de autenticación de Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    // Agregar detalles de la petición
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // Establecer la autenticación en el contexto de Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            
            // Continuar con el siguiente filtro en la cadena
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            // Si el token es inválido (firma incorrecta, expirado, etc.)
            logger.error("Error en autenticación JWT: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token JWT inválido o expirado");
        }
    }
}
