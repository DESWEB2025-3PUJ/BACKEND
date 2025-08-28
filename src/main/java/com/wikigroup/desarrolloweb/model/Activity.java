package com.wikigroup.desarrolloweb.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    private String name;
    private Double x;
    private Double y;
    private String description;
    private Double width;
    private Double height;
    private String status;

    @ManyToOne
    @JoinColumn(name = "process_id", nullable = false)
    private Process process;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> outgoingEdges;

    @OneToMany(mappedBy = "destiny", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> incomingEdges;

    // Getters y setters
}
