package com.wikigroup.desarrolloweb.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wikigroup.desarrolloweb.dtos.*;
import com.wikigroup.desarrolloweb.model.*;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Cuando se hagan PATCH/updates parciales:
        // mapper.getConfiguration().setSkipNullEnabled(true);

        // Empresa -> EmpresaDto (no tiene relaciones que mapear a IDs)
        mapper.typeMap(Empresa.class, EmpresaDto.class);

        // Usuario -> UsuarioDto (null-safe)
        mapper.typeMap(Usuario.class, UsuarioDto.class).addMappings(m -> {
            m.map(src -> src.getEmpresa() != null ? src.getEmpresa().getId() : null, UsuarioDto::setEmpresaId);
        });

        // Role -> RoleDto (null-safe)
        mapper.typeMap(Role.class, RoleDto.class).addMappings(m -> {
            m.map(src -> src.getEmpresa() != null ? src.getEmpresa().getId() : null, RoleDto::setEmpresaId);
        });

        // Gateway -> GatewayDto (null-safe)
        mapper.typeMap(Gateway.class, GatewayDto.class).addMappings(m -> {
            m.map(src -> src.getProcess() != null ? src.getProcess().getId() : null, GatewayDto::setProcessId);
        });

        // Activity -> ActivityDto (null-safe)
        mapper.typeMap(Activity.class, ActivityDto.class).addMappings(m -> {
            m.map(src -> src.getProcess() != null ? src.getProcess().getId() : null, ActivityDto::setProcessId);
            m.map(src -> src.getRole() != null ? src.getRole().getId() : null, ActivityDto::setRoleId);
        });

        // Edge -> EdgeDto (null-safe)
        mapper.typeMap(Edge.class, EdgeDto.class).addMappings(m -> {
            m.map(src -> src.getActivitySource() != null ? src.getActivitySource().getId() : null, EdgeDto::setActivitySourceId);
            m.map(src -> src.getActivityDestiny() != null ? src.getActivityDestiny().getId() : null, EdgeDto::setActivityDestinyId);
            m.map(src -> src.getProcess() != null ? src.getProcess().getId() : null, EdgeDto::setProcessId);
        });

        // Process -> ProcessDto (null-safe)
        mapper.typeMap(BpmProcess.class, ProcessDto.class).addMappings(m -> {
            m.map(src -> src.getEmpresa() != null ? src.getEmpresa().getId() : null, ProcessDto::setEmpresaId);
            m.map(src -> src.getRole() != null ? src.getRole().getId() : null, ProcessDto::setRoleId);
        });

        return mapper;
    }
}
