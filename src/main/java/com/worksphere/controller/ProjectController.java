package com.worksphere.controller;

import com.worksphere.entity.Project;
import com.worksphere.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository repository;
    public ProjectController(ProjectRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Project> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Project getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project create(@Valid @RequestBody Project project) {
        project.setId(null);
        validateDates(project);
        return repository.save(project);
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable Long id, @Valid @RequestBody Project input) {
        Project current = getById(id);
        input.setId(id);
        validateDates(input);
        current.setName(input.getName());
        current.setDescription(input.getDescription());
        current.setStatus(input.getStatus());
        current.setStartDate(input.getStartDate());
        current.setEndDate(input.getEndDate());
        return repository.save(current);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        repository.deleteById(id);
    }

    private void validateDates(Project p) {
        if (p.getEndDate() != null && p.getEndDate().isBefore(p.getStartDate()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
    }
}
