package com.wikigroup.desarrolloweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "edges")
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String status;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    @ManyToOne
    @JoinColumn(name = "activity_source_id")
    private Activity sourceActivity;

    @ManyToOne
    @JoinColumn(name = "activity_destiny_id")
    private Activity destinyActivity;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Process getProcess() { return process; }
    public void setProcess(Process process) { this.process = process; }

    public Activity getSourceActivity() { return sourceActivity; }
    public void setSourceActivity(Activity sourceActivity) { this.sourceActivity = sourceActivity; }

    public Activity getDestinyActivity() { return destinyActivity; }
    public void setDestinyActivity(Activity destinyActivity) { this.destinyActivity = destinyActivity; }
}