package com.wikigroup.desarrolloweb.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private EmpresaDto empresa;
    private UsuarioDto usuario;
}