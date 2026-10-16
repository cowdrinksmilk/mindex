package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CompensationServiceImpl
        implements CompensationService {

    private final CompensationRepository compensationRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Creates the service with access to compensation and employee data.
     */
    public CompensationServiceImpl(
            CompensationRepository compensationRepository,
            EmployeeRepository employeeRepository
    ) {
        this.compensationRepository = compensationRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Validates and saves compensation for an existing employee.
     */
    @Override
    public Compensation create(Compensation compensation) {
        validateCompensation(compensation);

        String employeeId =
                compensation.getEmployee().getEmployeeId();

        Employee employee = findEmployee(employeeId);

        compensationRepository
                .findByEmployee_EmployeeId(employeeId)
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Compensation already exists for employeeId: "
                                    + employeeId
                    );
                });

        compensation.setCompensationId(
                UUID.randomUUID().toString()
        );

        /*
         * Replaces the ID-only employee reference with the complete
         * employee record stored in the database.
         */
        compensation.setEmployee(employee);

        return compensationRepository.save(compensation);
    }

    /**
     * Returns the compensation associated with an employee.
     */
    @Override
    public Compensation read(String employeeId) {
        findEmployee(employeeId);

        return compensationRepository
                .findByEmployee_EmployeeId(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Compensation not found for employeeId: "
                                + employeeId
                ));
    }

    /**
     * Validates the required compensation fields.
     */
    private void validateCompensation(Compensation compensation) {
        if (compensation == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Compensation request body is required"
            );
        }

        if (compensation.getEmployee() == null
                || compensation.getEmployee().getEmployeeId() == null
                || compensation.getEmployee()
                        .getEmployeeId()
                        .isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Employee ID is required"
            );
        }

        if (compensation.getSalary() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Salary is required"
            );
        }

        if (compensation.getSalary()
                .compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Salary cannot be negative"
            );
        }

        if (compensation.getEffectiveDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Effective date is required"
            );
        }
    }

    /**
     * Finds an employee or returns 404 when the ID does not exist.
     */
    private Employee findEmployee(String employeeId) {
        Employee employee =
                employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Employee not found for employeeId: "
                            + employeeId
            );
        }

        return employee;
    }
}