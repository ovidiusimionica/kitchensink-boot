package com.example.kitchensinkboot.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Objects;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KitchenSinkBootApplicationCompatibilityTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String referenceSystemUrl;


    @SuppressWarnings("resource")
    static GenericContainer<?> container = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromClasspath("Dockerfile", "Dockerfile")
            .withFileFromClasspath("reference-app/kitchensink.war", "reference-app/kitchensink.war"))
            .withExposedPorts(8080);

    @SuppressWarnings("HttpUrlsUsage")
    @BeforeAll
    static void setUp() {
        container.start();
        referenceSystemUrl = String.format("http://%s:%d/kitchensink", container.getHost(),
                container.getMappedPort(8080));
    }

    @AfterAll
    static void tearDown() {
        container.close();
    }

    @Test
    @Order(1)
    void contextLoads() {
        // noop this is a smoke test to check that the spring context is loading

        String result = restTemplate.getForObject(referenceSystemUrl + "/rest/members", String.class);
        System.out.println(result);
        String second = restTemplate.getForObject("/rest/members", String.class);
        System.out.println(second);
    }


    @Test
    @Order(2)
    void whenNoMembersReturnsEmptyResponse() {
        assertThat(RegistrationRequest.getAllMembers(restTemplate, referenceSystemUrl), arrayWithSize(0));
        assertThat(RegistrationRequest.getAllMembers(restTemplate), arrayWithSize(0));
    }

    @Test
    @Order(3)
    void whenNewMemberRegistrationIsInvalidReturnsViolations() {
        TestMemberDTO malformedEmailAndPhone = new TestMemberDTO("name", "email", "125");
        assertCreationResponse(malformedEmailAndPhone, String.class);
        TestMemberDTO malformedEmailAndPhoneWithDigits = new TestMemberDTO("name", "email", "1234567890.5");
        assertCreationResponse(malformedEmailAndPhoneWithDigits, String.class);
        TestMemberDTO malformedNameTooLong = new TestMemberDTO(random(26, true, false), "email", "1234567890");
        assertCreationResponse(malformedNameTooLong, String.class);
        TestMemberDTO malformedNameWithNumbers = new TestMemberDTO(random(8, true, true), "email", "1234567890");
        assertCreationResponse(malformedNameWithNumbers, String.class);
        TestMemberDTO malformedNameEmpty = new TestMemberDTO("", "email", "1234567890");
        assertCreationResponse(malformedNameEmpty, String.class);
        TestMemberDTO malformedEmailEmpty = new TestMemberDTO("name", "", "1234567890");
        assertCreationResponse(malformedEmailEmpty, String.class);
    }


    @Test
    @Order(4)
    void whenNewMemberRegistrationIsValidReturnsContent() {
        TestMemberDTO validRecord = new TestMemberDTO("name", "email@example.com", "1234567890");
        assertCreationResponse(validRecord, TestMemberDTO.class);
    }

    @Test
    @Order(5)
    void whenNewMemberRegistrationIsEmailTakenReturnsConflict() {
        TestMemberDTO validRecord = new TestMemberDTO("name", "email@example.com", "1234567890");
        assertCreationResponse(validRecord, String.class);
    }

    @Test
    @Order(6)
    void whenMembersExistReturnsContentResponse() {
        assertThat(RegistrationRequest.getAllMembers(restTemplate, referenceSystemUrl), arrayWithSize(1));
        assertThat(RegistrationRequest.getAllMembers(restTemplate), arrayWithSize(1));
        assertThat(RegistrationRequest.getAllMembers(restTemplate), arrayContainingInAnyOrder(
                RegistrationRequest.getAllMembers(restTemplate, referenceSystemUrl)));
    }

    @Test
    @Order(7)
    void whenFindMemberByIdValidReturnsContent() {
        TestMemberDTO referenceRecord = RegistrationRequest.getAllMembers(restTemplate, referenceSystemUrl)[0];
        TestMemberDTO record = RegistrationRequest.getAllMembers(restTemplate)[0];

        TestMemberDTO referenceFoundRecord = RegistrationRequest.findById(restTemplate, referenceSystemUrl,
                referenceRecord.id).getBody();
        TestMemberDTO foundRecord = RegistrationRequest.findById(restTemplate, record.id).getBody();
        assertThat(foundRecord, notNullValue());
        assertThat(foundRecord, equalTo(referenceFoundRecord));
    }

    @Test
    @Order(8)
    void whenFindMemberByIdInvalidReturnsNotFound() {
        String invalidId = "invalidId";
        ResponseEntity<TestMemberDTO> referenceFoundRecord = RegistrationRequest.findById(restTemplate,
                referenceSystemUrl, invalidId);
        ResponseEntity<TestMemberDTO> foundRecord = RegistrationRequest.findById(restTemplate, invalidId);
        assertThat(foundRecord.getBody(), nullValue());
        assertThat(foundRecord.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(foundRecord.getBody(), equalTo(referenceFoundRecord.getBody()));
        assertThat(foundRecord.getStatusCode(), equalTo(referenceFoundRecord.getStatusCode()));
    }

    private <T> void assertCreationResponse(TestMemberDTO malformedMember, Class<T> expectedClass) {
        ResponseEntity<T> referenceResponse = RegistrationRequest.createMember(restTemplate, referenceSystemUrl,
                malformedMember, expectedClass);
        ResponseEntity<T> response = RegistrationRequest.createMember(restTemplate, malformedMember, expectedClass);
        assertThat(response.getStatusCode(), equalTo(referenceResponse.getStatusCode()));
        assertThat(response.getBody(), equalTo(referenceResponse.getBody()));
    }

    private static class RegistrationRequest {

        private static final String BASE_URL = "/rest/members";

        static TestMemberDTO[] getAllMembers(TestRestTemplate restTemplate, String referenceSystemUrl) {
            return restTemplate.getForObject(referenceSystemUrl + BASE_URL, TestMemberDTO[].class);
        }

        static TestMemberDTO[] getAllMembers(TestRestTemplate restTemplate) {
            return restTemplate.getForObject(BASE_URL, TestMemberDTO[].class);
        }

        static <T> ResponseEntity<T> createMember(TestRestTemplate restTemplate, String referenceSystemUrl,
                TestMemberDTO member, Class<T> clazz) {
            return restTemplate.postForEntity(referenceSystemUrl + BASE_URL, member, clazz);
        }

        static <T> ResponseEntity<T> createMember(TestRestTemplate restTemplate, TestMemberDTO member, Class<T> clazz) {
            return restTemplate.postForEntity(BASE_URL, member, clazz);
        }

        public static ResponseEntity<TestMemberDTO> findById(TestRestTemplate restTemplate, String referenceSystemUrl,
                String id) {
            return restTemplate.getForEntity(referenceSystemUrl + BASE_URL + "/" + id, TestMemberDTO.class);
        }

        public static ResponseEntity<TestMemberDTO> findById(TestRestTemplate restTemplate, String id) {
            return restTemplate.getForEntity(BASE_URL + "/" + id, TestMemberDTO.class);
        }
    }


    static final class TestMemberDTO {

        private final String id;
        private final String name;
        private final String email;
        private final String phoneNumber;

        public TestMemberDTO() {
            this(null, null, null, null);
        }

        public TestMemberDTO(String name, String email, String phoneNumber) {
            this(null, name, email, phoneNumber);
        }

        public TestMemberDTO(String id, String name, String email, String phoneNumber) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            var that = (TestMemberDTO) obj;
            return Objects.equals(this.name, that.name) &&
                    Objects.equals(this.email, that.email) &&
                    Objects.equals(this.phoneNumber, that.phoneNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, email, phoneNumber);
        }

        @Override
        public String toString() {
            return "TestMemberDTO[" +
                    "name=" + name + ", " +
                    "email=" + email + ", " +
                    "phoneNumber=" + phoneNumber + ']';
        }
    }


}
