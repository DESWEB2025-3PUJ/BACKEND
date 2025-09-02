package com.wikigroup.desarrolloweb.config;

import com.wikigroup.desarrolloweb.dtos.ActivityDto;
import com.wikigroup.desarrolloweb.dtos.EdgeDto;
import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.model.Edge;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Activity → ActivityDto
        mapper.typeMap(Activity.class, ActivityDto.class).addMappings(m ->
            m.map(src -> src.getProcess().getId(), ActivityDto::setProcessId)
        );

        // Edge → EdgeDto
        mapper.typeMap(Edge.class, EdgeDto.class).addMappings(m -> {
            m.map(src -> src.getActivitySource().getId(), EdgeDto::setActivitySourceId);
            m.map(src -> src.getActivityDestiny().getId(), EdgeDto::setActivityDestinyId);
            m.map(src -> src.getProcess().getId(), EdgeDto::setProcessId);
        });

        return mapper;
    }
}