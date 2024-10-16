package com.example.kitchensinkboot.web;

import static com.example.kitchensinkboot.web.ControllerConstants.BASE_URL;

import com.example.kitchensinkboot.model.member.Member;
import com.example.kitchensinkboot.service.DuplicateMemberException;
import com.example.kitchensinkboot.service.MemberRegistrationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("ClassEscapesDefinedScope")
@RestController
@RequestMapping(BASE_URL + "/members")
public class MemberRegistrationController {

  @Autowired
  private MemberRegistrationService memberRegistrationService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MemberDto>> listAllMembers() {
    return ResponseEntity.ok(memberRegistrationService.getAll().stream().map(this::toDto).toList());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createMember(@RequestBody MemberDto member) {
    try {
      memberRegistrationService.register(member.toModel());
      return ResponseEntity.ok().build();
    } catch (DuplicateMemberException e) {
      Map<String, String> responseObj = new HashMap<>();
      responseObj.put("email", "Email taken");
      return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
    }
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> lookupMemberById(@PathVariable("id") String id) {
    Member member = memberRegistrationService.findById(id);
    if (member == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(toDto(member));
  }

  private MemberDto toDto(Member member) {
    return new MemberDto(
        member.getId(),
        member.getName(),
        member.getEmail(),
        member.getPhoneNumber()
    );
  }

  private record MemberDto(String id, String name, String email, String phoneNumber) {

    public Member toModel() {
      return new Member(name, email, phoneNumber);
    }
  }
}
