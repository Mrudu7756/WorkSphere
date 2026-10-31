package com.worksphere.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task title is required")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotBlank(message = "Status is required")
    @Column(nullable = false, length = 30)
    private String status = "TODO";

    @NotNull(message = "Project ID is required")
    @Column(nullable = false)
    private Long projectId;

    @NotNull(message = "Assignee employee ID is required")
    @Column(nullable = false)
    private Long assigneeEmployeeId;

    private LocalDate dueDate;

    public Task() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getAssigneeEmployeeId() { return assigneeEmployeeId; }
    public void setAssigneeEmployeeId(Long assigneeEmployeeId) { this.assigneeEmployeeId = assigneeEmployeeId; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
