package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ReportingStructureServiceImplTest {

    private static final String JOHN_LENNON_ID =
            "16a596ae-edd3-4847-99fe-c4518e82c86f";

    private static final String RINGO_STARR_ID =
            "03aa1462-ffa9-4978-901b-7c001562cf6f";

    private static final String PAUL_MCCARTNEY_ID =
            "b7839309-3348-463b-a7e3-5de1c168beb3";

    private static final String INVALID_EMPLOYEE_ID =
            "invalid-employee-id";

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    /**
     * Verifies that John has four direct and indirect reports.
     */
    @Test
    public void testJohnLennonHasFourReports() {
        ReportingStructure result =
                reportingStructureService.read(JOHN_LENNON_ID);

        assertNotNull(result);
        assertNotNull(result.getEmployee());
        assertEquals(
                JOHN_LENNON_ID,
                result.getEmployee().getEmployeeId()
        );
        assertEquals(4, result.getNumberOfReports());
    }

    /**
     * Verifies that Ringo has two direct reports.
     */
    @Test
    public void testRingoStarrHasTwoReports() {
        ReportingStructure result =
                reportingStructureService.read(RINGO_STARR_ID);

        assertNotNull(result);
        assertNotNull(result.getEmployee());
        assertEquals(
                RINGO_STARR_ID,
                result.getEmployee().getEmployeeId()
        );
        assertEquals(2, result.getNumberOfReports());
    }

    /**
     * Verifies that an employee without reports returns zero.
     */
    @Test
    public void testPaulMcCartneyHasNoReports() {
        ReportingStructure result =
                reportingStructureService.read(PAUL_MCCARTNEY_ID);

        assertNotNull(result);
        assertNotNull(result.getEmployee());
        assertEquals(
                PAUL_MCCARTNEY_ID,
                result.getEmployee().getEmployeeId()
        );
        assertEquals(0, result.getNumberOfReports());
    }

    /**
     * Verifies that the endpoint returns John's reporting structure.
     */
    @Test
    public void testReportingStructureEndpoint() {
        String url = "http://localhost:"
                + port
                + "/reportingStructure/"
                + JOHN_LENNON_ID;

        ResponseEntity<ReportingStructure> response =
                restTemplate.getForEntity(
                        url,
                        ReportingStructure.class
                );

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        ReportingStructure result = response.getBody();

        assertNotNull(result);
        assertNotNull(result.getEmployee());
        assertEquals(
                JOHN_LENNON_ID,
                result.getEmployee().getEmployeeId()
        );
        assertEquals(4, result.getNumberOfReports());
    }

    /**
     * Verifies that an unknown employee returns 404.
     */
    @Test
    public void testInvalidEmployeeReturnsNotFound() {
        String url = "http://localhost:"
                + port
                + "/reportingStructure/"
                + INVALID_EMPLOYEE_ID;

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );
    }
}