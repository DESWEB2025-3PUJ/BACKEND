package com.wikigroup.desarrolloweb.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private Long empresaId;
}


