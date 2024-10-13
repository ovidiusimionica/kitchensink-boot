package com.example.kitchensinkboot.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.kitchensinkboot.model.member.Member;
import com.example.kitchensinkboot.service.DuplicateMemberException;
import com.example.kitchensinkboot.service.MemberRegistrationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@KitchenSinkWebTest(MemberRegistrationController.class)
class MemberRegistrationControllerTest {

  protected static final String MEMBER_ID = "-1";
  private static final String PATH = "/rest/members";
  private static final String VIOLATION_MESSAGE = "mockMessage";
  private static final String VIOLATION_PATH = "mockPath";
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MemberRegistrationService memberRegistrationService;

  private static ConstraintViolation<?> mockConstraintViolation() {
    ConstraintViolation<?> mockViolation = mock();
    Path mockPath = mock();
    when(mockViolation.getMessage()).thenReturn(VIOLATION_MESSAGE);
    when(mockPath.toString()).thenReturn(VIOLATION_PATH);
    when(mockViolation.getPropertyPath()).thenReturn(mockPath);
    return mockViolation;
  }

  @Test
  void whenNoMembersReturnsEmptyResponse() throws Exception {
    assertThat(RegistrationRequest.getAllMembers(mockMvc, objectMapper), empty());
  }

  @Test
  void whenMembersExistReturnsContentResponse() throws Exception {
    Member expectedMember = new Member("name", "email", "phone");
    when(memberRegistrationService.getAll()).thenReturn(List.of(expectedMember));
    assertThat(RegistrationRequest.getAllMembers(mockMvc, objectMapper), is(
        List.of(
            new TestMemberDto(expectedMember.getName(), expectedMember.getEmail(),
                expectedMember.getPhoneNumber())
        )
    ));
  }

  @Test
  void whenNewMemberRegistrationIsInvalidReturnsViolations() throws Exception {
    TestMemberDto malformedMember = new TestMemberDto("name", "email", "phone");
    ConstraintViolation<?> constraintViolation = mockConstraintViolation();
    when(memberRegistrationService.register(any())).thenAnswer(invocationOnMock -> {
      throw new ConstraintViolationException(Set.of(constraintViolation));
    });
    assertCreateMember(malformedMember, Map.of(VIOLATION_PATH, VIOLATION_MESSAGE), BAD_REQUEST);
  }

  @Test
  void whenNewMemberRegistrationIsEmailTakenReturnsConflict() throws Exception {
    TestMemberDto malformedMember = new TestMemberDto("name", "email", "phone");
    when(memberRegistrationService.register(any())).thenThrow(new DuplicateMemberException());
    assertCreateMember(malformedMember, Map.of("email", "Email taken"), CONFLICT);
  }

  @Test
  void whenNewMemberRegistrationIsValidReturnsOkNoContent() throws Exception {
    Member expectedMember = new Member("name", "email", "phone");
    TestMemberDto malformedMember = new TestMemberDto(expectedMember.getName(), expectedMember.getEmail(),
        expectedMember.getPhoneNumber());
    when(memberRegistrationService.register(any())).thenReturn(expectedMember);
    assertCreateMember(malformedMember);
  }

  @Test
  void whenFindMemberByIdValidReturnsContent() throws Exception {
    Member expectedMember = new Member("name", "email", "phone");
    TestMemberDto expectedMemberTest = new TestMemberDto(expectedMember.getName(), expectedMember.getEmail(),
        expectedMember.getPhoneNumber());
    when(memberRegistrationService.findById(eq(MEMBER_ID))).thenReturn(expectedMember);
    assertThat(RegistrationRequest.getMemberById(mockMvc, objectMapper, OK),
        is(expectedMemberTest));
  }

  @Test
  void whenFindMemberByIdInvalidReturnsNotFound() throws Exception {
    when(memberRegistrationService.findById(eq(MEMBER_ID))).thenReturn(null);
    assertThat(RegistrationRequest.getMemberById(mockMvc, objectMapper, NOT_FOUND), nullValue());
  }

  private <T> void assertCreateMember(TestMemberDto memberDto, T expectedResponse, HttpStatus expectedStatus)
      throws Exception {
    assertThat(objectMapper.readValue(
            RegistrationRequest.createMember(mockMvc, objectMapper, memberDto, expectedStatus),
            expectedResponse.getClass()),
        equalTo(expectedResponse));
  }

  private void assertCreateMember(TestMemberDto memberDto)
      throws Exception {
    assertThat(RegistrationRequest.createMember(mockMvc, objectMapper, memberDto, OK),
        equalTo(""));
  }


  private static class RegistrationRequest {

    static List<TestMemberDto> getAllMembers(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON))
          .andReturn();

      return objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
          new TypeReference<>() {
          });
    }

    static TestMemberDto getMemberById(MockMvc mockMvc, ObjectMapper objectMapper,
                                       HttpStatus expectedStatus) throws Exception {
      MvcResult mvcResult = mockMvc.perform(
              MockMvcRequestBuilders.get(
                  PATH + "/" + MemberRegistrationControllerTest.MEMBER_ID))
          .andExpect(status().is(expectedStatus.value()))
          .andReturn();

      if (expectedStatus == OK) {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
            new TypeReference<>() {
            });
      } else {
        return null;
      }
    }

    static String createMember(MockMvc mockMvc, ObjectMapper objectMapper, TestMemberDto memberDto,
                               HttpStatus expectedStatus) throws Exception {
      MvcResult mvcResult = mockMvc.perform(
              MockMvcRequestBuilders.post(PATH)
                  .content(objectMapper.writeValueAsString(memberDto))
                  .contentType(APPLICATION_JSON))
          .andExpect(status().is(expectedStatus.value()))
          .andReturn();
      return mvcResult.getResponse().getContentAsString();
    }
  }

  private record TestMemberDto(String name, String email, String phoneNumber) {

  }
}