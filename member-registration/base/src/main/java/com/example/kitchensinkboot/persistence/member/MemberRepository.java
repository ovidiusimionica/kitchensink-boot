package com.example.kitchensinkboot.persistence.member;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;

import com.example.kitchensinkboot.model.member.Member;

public interface MemberRepository {

    Member insert(Member member) throws DuplicateKeyException;

    List<Member> getAll();

    Member findById(String id);
}
