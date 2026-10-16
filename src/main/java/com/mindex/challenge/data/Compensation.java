package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Stores compensation information for an employee.
 */
@Document(collection = "compensation")
public class Compensation {

    @Id
    private String compensationId;

    private Employee employee;
    private BigDecimal salary;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    /**
     * Creates an empty compensation record.
     */
    public Compensation() {
    }

    /**
     * Creates a compensation record with all fields.
     */
    public Compensation(
            String compensationId,
            Employee employee,
            BigDecimal salary,
            LocalDate effectiveDate
    ) {
        this.compensationId = compensationId;
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    /**
     * Returns the compensation ID.
     */
    public String getCompensationId() {
        return compensationId;
    }

    /**
     * Sets the compensation ID.
     */
    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }

    /**
     * Returns the employee associated with the compensation.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the employee associated with the compensation.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Returns the employee's salary.
     */
    public BigDecimal getSalary() {
        return salary;
    }

    /**
     * Sets the employee's salary.
     */
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    /**
     * Returns the date when the compensation becomes effective.
     */
    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the date when the compensation becomes effective.
     */
    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}