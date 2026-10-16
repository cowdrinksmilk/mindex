package com.mindex.challenge.data;

/**
 * Holds an employee and the total number of reports below them.
 */
public class ReportingStructure {

    private Employee employee;
    private int numberOfReports;

    /**
     * Creates an empty reporting structure.
     */
    public ReportingStructure() {
    }

    /**
     * Creates a reporting structure with its calculated values.
     */
    public ReportingStructure(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    /**
     * Returns the employee at the top of the reporting structure.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the employee at the top of the reporting structure.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Returns the total number of direct and indirect reports.
     */
    public int getNumberOfReports() {
        return numberOfReports;
    }

    /**
     * Sets the total number of direct and indirect reports.
     */
    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}