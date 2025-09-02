package com.wikigroup.desarrolloweb.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GatewayDto {
    private Long id;
    private String tipo;
    private String descripcion;
    private Long processId;
}

