package com.wikigroup.desarrolloweb.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long empresaId;
}

