package com.worksphere.controller;

import com.worksphere.entity.Task;
import com.worksphere.repository.EmployeeRepository;
import com.worksphere.repository.ProjectRepository;
import com.worksphere.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository tasks;
    private final ProjectRepository projects;
    private final EmployeeRepository employees;

    public TaskController(TaskRepository tasks, ProjectRepository projects, EmployeeRepository employees) {
        this.tasks = tasks; this.projects = projects; this.employees = employees;
    }

    @GetMapping
    public List<Task> getAll(@RequestParam(required = false) Long projectId,
                             @RequestParam(required = false) Long employeeId) {
        if (projectId != null) return tasks.findByProjectId(projectId);
        if (employeeId != null) return tasks.findByAssigneeEmployeeId(employeeId);
        return tasks.findAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return tasks.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody Task task) {
        validateReferences(task);
        task.setId(null);
        return tasks.save(task);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody Task input) {
        Task current = getById(id);
        validateReferences(input);
        current.setTitle(input.getTitle());
        current.setDescription(input.getDescription());
        current.setStatus(input.getStatus());
        current.setProjectId(input.getProjectId());
        current.setAssigneeEmployeeId(input.getAssigneeEmployeeId());
        current.setDueDate(input.getDueDate());
        return tasks.save(current);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!tasks.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        tasks.deleteById(id);
    }

    private void validateReferences(Task task) {
        if (!projects.existsById(task.getProjectId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project ID does not exist");
        if (!employees.existsById(task.getAssigneeEmployeeId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignee employee ID does not exist");
    }
}
