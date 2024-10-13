package com.example.kitchensinkboot.app;

import com.example.kitchensinkboot.model.member.Member;
import com.example.kitchensinkboot.service.DuplicateMemberException;
import com.example.kitchensinkboot.service.MemberRegistrationService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@Import({TestcontainersConfiguration.class})
@SpringBootTest
class KitchenSinkBootApplicationTests {

  @Autowired
  private MemberRegistrationService memberService;

  @Autowired
  private MongoTemplate mongoTemplate;

  @BeforeEach
  void cleanDatabase() {
    mongoTemplate.getCollectionNames().forEach((collection) -> mongoTemplate.remove(new Query(), collection));
  }


  @Test
  void contextLoads() {
    // noop this is a smoke test to check that the spring context is loading
  }

  @Test
  void insertMember() {
    memberService.register(new Member("ion", "email@example.com", "12345667890"));
  }

  @Test
  void insertMemberTwice() {
    memberService.register(new Member("ion", "email@example.com", "12345667890"));
    Assert.assertThrows(DuplicateMemberException.class,
        () -> memberService.register(new Member("ion", "email@example.com", "12345667890")));

  }

}
