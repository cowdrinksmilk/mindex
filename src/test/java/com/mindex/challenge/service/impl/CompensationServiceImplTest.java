package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CompensationServiceImplTest {

    private static final String JOHN_LENNON_ID =
            "16a596ae-edd3-4847-99fe-c4518e82c86f";

    private static final String INVALID_EMPLOYEE_ID =
            "invalid-employee-id";

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    /**
     * Clears compensation data before each test.
     */
    @Before
    public void setUp() {
        compensationRepository.deleteAll();
    }

    /**
     * Verifies that compensation can be created and read.
     */
    @Test
    public void testCreateAndReadCompensation() {
        Compensation created =
                compensationService.create(createCompensation());

        assertNotNull(created);
        assertNotNull(created.getCompensationId());
        assertNotNull(created.getEmployee());

        assertEquals(
                JOHN_LENNON_ID,
                created.getEmployee().getEmployeeId()
        );

        assertEquals(
                0,
                new BigDecimal("120000.00")
                        .compareTo(created.getSalary())
        );

        assertEquals(
                LocalDate.of(2026, 10, 15),
                created.getEffectiveDate()
        );

        Compensation read =
                compensationService.read(JOHN_LENNON_ID);

        assertEquals(
                created.getCompensationId(),
                read.getCompensationId()
        );
    }

    /**
     * Verifies that the POST and GET endpoints persist compensation.
     */
    @Test
    public void testCompensationEndpoints() {
        String createUrl =
                "http://localhost:" + port + "/compensation";

        ResponseEntity<Compensation> createResponse =
                restTemplate.postForEntity(
                        createUrl,
                        createCompensation(),
                        Compensation.class
                );

        assertEquals(
                HttpStatus.OK,
                createResponse.getStatusCode()
        );

        Compensation created = createResponse.getBody();

        assertNotNull(created);
        assertNotNull(created.getCompensationId());

        String readUrl =
                createUrl + "/" + JOHN_LENNON_ID;

        ResponseEntity<Compensation> readResponse =
                restTemplate.getForEntity(
                        readUrl,
                        Compensation.class
                );

        assertEquals(
                HttpStatus.OK,
                readResponse.getStatusCode()
        );

        Compensation read = readResponse.getBody();

        assertNotNull(read);
        assertEquals(
                created.getCompensationId(),
                read.getCompensationId()
        );
    }

    /**
     * Verifies that an unknown employee returns 404.
     */
    @Test
    public void testUnknownEmployeeReturnsNotFound() {
        Employee employee = new Employee();
        employee.setEmployeeId(INVALID_EMPLOYEE_ID);

        Compensation compensation = new Compensation();
        compensation.setEmployee(employee);
        compensation.setSalary(
                new BigDecimal("120000.00")
        );
        compensation.setEffectiveDate(
                LocalDate.of(2026, 10, 15)
        );

        try {
            compensationService.create(compensation);
            fail("Expected employee lookup to return 404");
        } catch (ResponseStatusException exception) {
            assertEquals(
                    HttpStatus.NOT_FOUND,
                    exception.getStatusCode()
            );
        }
    }

    /**
     * Verifies that a negative salary returns 400.
     */
    @Test
    public void testNegativeSalaryReturnsBadRequest() {
        Compensation compensation = createCompensation();
        compensation.setSalary(new BigDecimal("-1.00"));

        try {
            compensationService.create(compensation);
            fail("Expected negative salary to return 400");
        } catch (ResponseStatusException exception) {
            assertEquals(
                    HttpStatus.BAD_REQUEST,
                    exception.getStatusCode()
            );
        }
    }

    /**
     * Verifies that compensation cannot be created twice.
     */
    @Test
    public void testDuplicateCompensationReturnsConflict() {
        compensationService.create(createCompensation());

        try {
            compensationService.create(createCompensation());
            fail("Expected duplicate compensation to return 409");
        } catch (ResponseStatusException exception) {
            assertEquals(
                    HttpStatus.CONFLICT,
                    exception.getStatusCode()
            );
        }
    }

    /**
     * Creates valid compensation test data.
     */
    private Compensation createCompensation() {
        Employee employee = new Employee();
        employee.setEmployeeId(JOHN_LENNON_ID);

        Compensation compensation = new Compensation();
        compensation.setEmployee(employee);
        compensation.setSalary(
                new BigDecimal("120000.00")
        );
        compensation.setEffectiveDate(
                LocalDate.of(2026, 10, 15)
        );

        return compensation;
    }
}