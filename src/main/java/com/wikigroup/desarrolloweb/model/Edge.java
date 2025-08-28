package com.wikigroup.desarrolloweb.model;

import jakarta.persistence.*;

@Entity
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
    private Process process;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Activity getActivitySource() { return activitySource; }
    public void setActivitySource(Activity activitySource) { this.activitySource = activitySource; }

    public Activity getActivityDestiny() { return activityDestiny; }
    public void setActivityDestiny(Activity activityDestiny) { this.activityDestiny = activityDestiny; }

    public Process getProcess() { return process; }
    public void setProcess(Process process) { this.process = process; }
}

