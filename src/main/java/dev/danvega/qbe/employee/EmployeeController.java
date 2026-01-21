package dev.danvega.qbe.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Search and query employees via QBE")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/search")
    @Operation(
            summary = "Search employees with optional filters",
            description = "Uses QBE with a custom matcher. When parameters are omitted, it returns all employees."
    )
    public ResponseEntity<List<Employee>> searchEmployees(
            @Parameter(description = "Exact match on first name")
            @RequestParam(required = false) String firstName,
            @Parameter(description = "Partial match on department")
            @RequestParam(required = false) String department) {

        List<Employee> employees = employeeService
                .findEmployeesWithCustomMatcher(firstName, department);
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/search/example")
    @Operation(
            summary = "Search employees by example",
            description = "Matches non-null fields on the request body using default QBE behavior."
    )
    public List<Employee> findByExample(@RequestBody @Valid Employee employee) {
        return employeeService.findEmployeesByExample(employee);
    }

    @PostMapping("/search/example/one")
    @Operation(
            summary = "Find a single employee by example",
            description = "Returns a single match or 404 when no employee matches the example."
    )
    public Employee findOneByExample(@RequestBody @Valid Employee employee) {
        return employeeService.findOneEmployeeByExample(employee)
                .orElseThrow(() -> new EmployeeNotFoundException("No employee found matching the example"));
    }

    @PostMapping("/count")
    @Operation(
            summary = "Count employees by example",
            description = "Counts matching employees using non-null example fields."
    )
    public long countByExample(@RequestBody @Valid Employee employee) {
        return employeeService.countEmployeesByExample(employee);
    }

    @PostMapping("/exists")
    @Operation(
            summary = "Check if any employees match the example",
            description = "Returns true if at least one employee matches the example."
    )
    public boolean existsByExample(@RequestBody @Valid Employee employee) {
        return employeeService.existsByExample(employee);
    }

}
