package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class ReportingStructureServiceImpl
        implements ReportingStructureService {

    private final EmployeeRepository employeeRepository;

    /**
     * Creates the service with access to employee data.
     */
    public ReportingStructureServiceImpl(
            EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Returns an employee and the total number of reports below them.
     */
    @Override
    public ReportingStructure read(String employeeId) {
        Employee employee = findEmployee(employeeId);

        Set<String> visitedEmployeeIds = new HashSet<>();
        visitedEmployeeIds.add(employeeId);

        return new ReportingStructure(
                employee,
                countReports(employee, visitedEmployeeIds)
        );
    }

    /**
     * Recursively counts all distinct direct and indirect reports.
     */
    private int countReports(
            Employee employee,
            Set<String> visitedEmployeeIds
    ) {
        if (employee.getDirectReports() == null) {
            return 0;
        }

        int numberOfReports = 0;

        for (Employee reportReference : employee.getDirectReports()) {
            if (reportReference == null
                    || reportReference.getEmployeeId() == null
                    || reportReference.getEmployeeId().isBlank()) {
                continue;
            }

            String reportId = reportReference.getEmployeeId();

            // Prevents duplicate counts and circular relationships.
            if (!visitedEmployeeIds.add(reportId)) {
                continue;
            }

            Employee report = findEmployee(reportId);
            numberOfReports +=
                    1 + countReports(report, visitedEmployeeIds);
        }

        return numberOfReports;
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