package com.wikigroup.desarrolloweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "activities")
@Getter
@Setter
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double x;
    private Double y;
    private String description;
    private Double width;
    private Double height;
    private String status;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private BpmProcess process;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String tipoActividad;
}
