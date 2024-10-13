package com.example.kitchensinkboot.persistence.member;

import com.example.kitchensinkboot.model.member.Member;
import java.util.List;
import org.springframework.dao.DuplicateKeyException;

public interface MemberRepository {

  Member insert(Member member) throws DuplicateKeyException;

  List<Member> getAll();

  Member findById(String id);
}
