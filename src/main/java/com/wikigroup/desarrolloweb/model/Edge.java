package com.wikigroup.desarrolloweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "edges")
@Getter
@Setter
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String status;

    @ManyToOne
    @JoinColumn(name = "activity_source_id")
    private Activity activitySource;

    @ManyToOne
    @JoinColumn(name = "activity_destiny_id")
    private Activity activityDestiny;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private BpmProcess process;
}

