package com.worksphere.controller;

import com.worksphere.entity.Employee;
import com.worksphere.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeRepository repository;
    public EmployeeController(EmployeeRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Employee> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@Valid @RequestBody Employee employee) {
        if (repository.existsByEmail(employee.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An employee with this email already exists");
        employee.setId(null);
        return repository.save(employee);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @Valid @RequestBody Employee input) {
        Employee current = getById(id);
        if (!current.getEmail().equals(input.getEmail()) && repository.existsByEmail(input.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An employee with this email already exists");
        current.setName(input.getName());
        current.setEmail(input.getEmail());
        current.setDepartment(input.getDepartment());
        current.setJobTitle(input.getJobTitle());
        return repository.save(current);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        repository.deleteById(id);
    }
}
