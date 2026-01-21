package dev.danvega.qbe.employee;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmployeeDataSeeder implements ApplicationRunner {

    private final EmployeeRepository employeeRepository;

    public EmployeeDataSeeder(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(transactionManager = "postgresTransactionManager")
    public void run(ApplicationArguments args) {
        if (employeeRepository.count() > 0) {
            return;
        }

        List<Employee> employees = List.of(
                employee("Jane", "Doe", "IT", "Developer", "75000.00"),
                employee("Mike", "Johnson", "IT", "Developer", "72000.00"),
                employee("John", "Smith", "IT", "Senior Developer", "95000.00"),
                employee("Emily", "Davis", "Engineering", "Engineer", "98000.00"),
                employee("David", "Miller", "Engineering", "Engineer", "78000.00"),
                employee("Lisa", "Wilson", "Engineering", "Engineer", "108000.00"),
                employee("Michael", "Taylor", "Engineering", "Engineer", "76000.00"),
                employee("Robert", "Brown", "HR", "Manager", "85000.00"),
                employee("Kevin", "Lee", "Marketing", "Manager", "88000.00"),
                employee("Laura", "Hall", "Sales", "Manager", "86000.00"),
                employee("Steven", "King", "Operations", "Manager", "90000.00"),
                employee("Thomas", "Smith", "Marketing", "Marketing Specialist", "62000.00"),
                employee("Anna", "Smith", "Sales", "Sales Representative", "58000.00"),
                employee("Robert", "Smith", "Customer Support", "Support Specialist", "54000.00"),
                employee("Johnny", "Wilson", "Sales", "Representative", "86000.00"),
                employee("William", "White", "HR", "HR Coordinator", "55000.00"),
                employee("Patricia", "Martinez", "Finance", "Financial Analyst", "78000.00"),
                employee("Elizabeth", "Robinson", "Finance", "Accountant", "68000.00")
        );

        employeeRepository.saveAll(employees);
    }

    private Employee employee(String firstName, String lastName, String department, String position, String salary) {
        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .position(position)
                .salary(new BigDecimal(salary))
                .build();
    }
}
