package com.wikigroup.desarrolloweb.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private Long id;
    private String name;
    private String description;
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private String status;
    private Long processId;
}

