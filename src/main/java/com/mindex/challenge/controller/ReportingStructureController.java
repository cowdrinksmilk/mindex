package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportingStructureController {

    private final ReportingStructureService reportingStructureService;

    /**
     * Creates the controller with access to reporting operations.
     */
    public ReportingStructureController(
            ReportingStructureService reportingStructureService
    ) {
        this.reportingStructureService = reportingStructureService;
    }

    /**
     * Returns an employee and the total number of reports below them.
     */
    @GetMapping("/reportingStructure/{employeeId}")
    public ReportingStructure read(
            @PathVariable String employeeId
    ) {
        return reportingStructureService.read(employeeId);
    }
}