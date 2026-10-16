package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompensationController {

    private final CompensationService compensationService;

    /**
     * Creates the controller with access to compensation operations.
     */
    public CompensationController(
            CompensationService compensationService
    ) {
        this.compensationService = compensationService;
    }

    /**
     * Creates and saves compensation.
     */
    @PostMapping("/compensation")
    public Compensation create(
            @RequestBody Compensation compensation
    ) {
        return compensationService.create(compensation);
    }

    /**
     * Returns compensation for the specified employee.
     */
    @GetMapping("/compensation/{employeeId}")
    public Compensation read(
            @PathVariable String employeeId
    ) {
        return compensationService.read(employeeId);
    }
}