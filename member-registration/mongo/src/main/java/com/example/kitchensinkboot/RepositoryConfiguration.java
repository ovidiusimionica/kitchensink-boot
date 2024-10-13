package com.example.kitchensinkboot;

import com.example.kitchensinkboot.persistence.member.MemberRepository;
import com.example.kitchensinkboot.persistence.member.MongoMemberRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConditionalOnProperty(name = "com.example.kitchecksinkboot.db.type", havingValue = "mongo")
public class RepositoryConfiguration {

  @Bean
  public MemberRepository memberRepository(MongoTemplate mongoTemplate) {
    return new MongoMemberRepository(mongoTemplate);
  }

}
