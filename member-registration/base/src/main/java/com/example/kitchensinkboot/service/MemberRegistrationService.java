package com.example.kitchensinkboot.service;

import com.example.kitchensinkboot.model.member.Member;
import com.example.kitchensinkboot.persistence.member.MemberRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class MemberRegistrationService {

  private static final Logger log = LoggerFactory.getLogger(MemberRegistrationService.class);
  private final MemberRepository memberRepository;
  private final Validator validator;

  public MemberRegistrationService(MemberRepository memberRepository, Validator validator) {
    this.memberRepository = memberRepository;
    this.validator = validator;
  }

  public Member register(Member member) {
    validateMember(member);
    try {
      return memberRepository.insert(member);
    } catch (DuplicateKeyException ex) {
      if (log.isTraceEnabled()) {
        log.trace(ex.getMessage(), ex);
      }
      throw new DuplicateMemberException();
    }
  }

  private void validateMember(Member member) throws ValidationException {
    // Create a bean validator and check for issues.
    Set<ConstraintViolation<Member>> violations = validator.validate(member);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(new HashSet<>(violations));
    }
  }


  public List<Member> getAll() {
    return memberRepository.getAll();
  }

  public Member findById(String id) {
    return memberRepository.findById(id);
  }
}
