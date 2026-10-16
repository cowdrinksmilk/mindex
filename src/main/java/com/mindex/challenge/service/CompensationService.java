package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

/**
 * Provides operations for employee compensation.
 */
public interface CompensationService {

    /**
     * Creates and saves compensation for an employee.
     */
    Compensation create(Compensation compensation);

    /**
     * Returns compensation for an employee.
     */
    Compensation read(String employeeId);
}