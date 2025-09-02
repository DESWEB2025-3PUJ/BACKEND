package com.wikigroup.desarrolloweb.config;

import com.wikigroup.desarrolloweb.dtos.*;
import com.wikigroup.desarrolloweb.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Empresa
        mapper.typeMap(Empresa.class, EmpresaDto.class);

        // Usuario
        mapper.typeMap(Usuario.class, UsuarioDto.class).addMappings(m -> {
            m.map(src -> src.getEmpresa().getId(), UsuarioDto::setEmpresaId);
        });

        // Role
        mapper.typeMap(Role.class, RoleDto.class).addMappings(m -> {
            m.map(src -> src.getEmpresa().getId(), RoleDto::setEmpresaId);
        });

        // Gateway
        mapper.typeMap(Gateway.class, GatewayDto.class).addMappings(m -> {
            m.map(src -> src.getProcess().getId(), GatewayDto::setProcessId);
        });

        // Activity
        mapper.typeMap(Activity.class, ActivityDto.class).addMappings(m -> {
            m.map(src -> src.getProcess().getId(), ActivityDto::setProcessId);
            m.map(src -> src.getRole().getId(), ActivityDto::setRoleId);
        });

        // Edge
        mapper.typeMap(Edge.class, EdgeDto.class).addMappings(m -> {
            m.map(src -> src.getActivitySource().getId(), EdgeDto::setActivitySourceId);
            m.map(src -> src.getActivityDestiny().getId(), EdgeDto::setActivityDestinyId);
            m.map(src -> src.getProcess().getId(), EdgeDto::setProcessId);
        });

        return mapper;
    }
}
