package com.wikigroup.desarrolloweb.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDto {
    private Long id;
    private String description;
    private String status;
    private Long activitySourceId;
    private Long activityDestinyId;
    private Long processId;
}
