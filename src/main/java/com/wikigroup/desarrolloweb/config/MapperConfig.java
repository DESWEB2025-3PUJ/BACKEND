package com.wikigroup.desarrolloweb.config;

import com.wikigroup.desarrolloweb.dtos.*;
import com.wikigroup.desarrolloweb.model.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.Conditions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Config general
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(Conditions.isNotNull()); // no sobreescribir con null

        // ====== Converters útiles ======
        // Usuario -> UsuarioDto: forzar password=null para no exponerlo
        Converter<Usuario, UsuarioDto> usuarioToDtoConverter = new Converter<>() {
            @Override
            public UsuarioDto convert(MappingContext<Usuario, UsuarioDto> ctx) {
                Usuario src = ctx.getSource();
                UsuarioDto dst = ctx.getDestination() != null ? ctx.getDestination() : new UsuarioDto();
                // Campos simples
                dst.setId(src.getId());
                dst.setNombre(src.getNombre());
                dst.setEmail(src.getEmail());
                dst.setRol(src.getRol());
                // Relaciones -> IDs
                dst.setEmpresaId(src.getEmpresa() != null ? src.getEmpresa().getId() : null);
                // Seguridad
                dst.setPassword(null);
                return dst;
            }
        };

        // ====== Entity -> DTO ======

        // Empresa -> EmpresaDto (campos simples)
        mapper.typeMap(Empresa.class, EmpresaDto.class);

        // Usuario -> UsuarioDto (null-safe + password null)
        mapper.createTypeMap(Usuario.class, UsuarioDto.class)
                .setConverter(usuarioToDtoConverter);

        // Role -> RoleDto
        mapper.createTypeMap(Role.class, RoleDto.class)
                .addMappings(m -> m.map(src -> src.getEmpresa() != null ? src.getEmpresa().getId() : null,
                        RoleDto::setEmpresaId));

        // Gateway -> GatewayDto
        mapper.createTypeMap(Gateway.class, GatewayDto.class)
                .addMappings(m -> m.map(src -> src.getProcess() != null ? src.getProcess().getId() : null,
                        GatewayDto::setProcessId));

        // Activity -> ActivityDto
        mapper.createTypeMap(Activity.class, ActivityDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getProcess() != null ? src.getProcess().getId() : null,
                            ActivityDto::setProcessId);
                    m.map(src -> src.getRole() != null ? src.getRole().getId() : null,
                            ActivityDto::setRoleId);
                });

        // Edge -> EdgeDto
        mapper.createTypeMap(Edge.class, EdgeDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getActivitySource() != null ? src.getActivitySource().getId() : null,
                            EdgeDto::setActivitySourceId);
                    m.map(src -> src.getActivityDestiny() != null ? src.getActivityDestiny().getId() : null,
                            EdgeDto::setActivityDestinyId);
                    m.map(src -> src.getProcess() != null ? src.getProcess().getId() : null,
                            EdgeDto::setProcessId);
                });

        // BpmProcess -> ProcessDto
        mapper.createTypeMap(BpmProcess.class, ProcessDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getEmpresa() != null ? src.getEmpresa().getId() : null,
                            ProcessDto::setEmpresaId);
                    m.map(src -> src.getRole() != null ? src.getRole().getId() : null,
                            ProcessDto::setRoleId);
                });

        // ====== DTO -> Entity (sin hidratar relaciones) ======

        // EmpresaDto -> Empresa
        mapper.typeMap(EmpresaDto.class, Empresa.class);

        // UsuarioDto -> Usuario (no setear Empresa aquí)
        mapper.createTypeMap(UsuarioDto.class, Usuario.class)
                .addMappings(m -> {
                    // Ignoramos relaciones; las setea el service
                    m.skip(Usuario::setEmpresa);
                });

        // RoleDto -> Role (no setear Empresa aquí)
        mapper.createTypeMap(RoleDto.class, Role.class)
                .addMappings(m -> m.skip(Role::setEmpresa));

        // GatewayDto -> Gateway (no setear Process aquí)
        mapper.createTypeMap(GatewayDto.class, Gateway.class)
                .addMappings(m -> m.skip(Gateway::setProcess));

        // ActivityDto -> Activity (no setear Process/Role aquí)
        mapper.createTypeMap(ActivityDto.class, Activity.class)
                .addMappings(m -> {
                    m.skip(Activity::setProcess);
                    m.skip(Activity::setRole);
                });

        // EdgeDto -> Edge (no setear relations aquí)
        mapper.createTypeMap(EdgeDto.class, Edge.class)
                .addMappings(m -> {
                    m.skip(Edge::setActivitySource);
                    m.skip(Edge::setActivityDestiny);
                    m.skip(Edge::setProcess);
                });

        // ProcessDto -> BpmProcess (no setear Empresa/Role aquí)
        mapper.createTypeMap(ProcessDto.class, BpmProcess.class)
                .addMappings(m -> {
                    m.skip(BpmProcess::setEmpresa);
                    m.skip(BpmProcess::setRole);
                });

        return mapper;
    }
}
