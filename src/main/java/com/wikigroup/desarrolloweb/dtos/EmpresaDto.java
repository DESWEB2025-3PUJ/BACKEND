package com.wikigroup.desarrolloweb.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para Empresa según HU-01: Registro de empresa
 */
@Getter
@Setter
public class EmpresaDto {
    private Long id;
    private String nombre;
    private String nit;  // HU-01: NIT u otro identificador único
    private String correoContacto;  // HU-01: Correo de contacto
    private String descripcion;
}
