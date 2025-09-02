package com.wikigroup.desarrolloweb.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDto {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String categoria;
    private String estado;
    private Long empresaId;
}

