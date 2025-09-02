package com.wikigroup.desarrolloweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gateways")
@Getter
@Setter
public class Gateway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // exclusivo, inclusivo, paralelo
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

}
