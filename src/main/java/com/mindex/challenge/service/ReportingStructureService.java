package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

/**
 * Provides operations for employee reporting structures.
 */
public interface ReportingStructureService {

    /**
     * Calculates the reporting structure for an employee.
     */
    ReportingStructure read(String employeeId);
}