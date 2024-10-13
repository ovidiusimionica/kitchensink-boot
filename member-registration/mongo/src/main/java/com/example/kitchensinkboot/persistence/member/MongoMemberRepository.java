package com.example.kitchensinkboot.persistence.member;

import com.example.kitchensinkboot.model.member.Member;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

public class MongoMemberRepository implements MemberRepository {


  private static final String COLLECTION_NAME = "members";
  private final MongoTemplate mongoTemplate;

  public MongoMemberRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
    initIndexes();
  }

  @Override
  public Member insert(Member member) {
    return mongoTemplate.insert(member, COLLECTION_NAME);
  }

  @Override
  public List<Member> getAll() {
    return mongoTemplate.findAll(Member.class, COLLECTION_NAME);
  }

  @Override
  public Member findById(String id) {
    return mongoTemplate.findById(id, Member.class, COLLECTION_NAME);
  }


  private void initIndexes() {
    // Define unique index on the "email" field of the "user" collection
    mongoTemplate.indexOps(COLLECTION_NAME)
        .ensureIndex(new Index("email", org.springframework.data.domain.Sort.Direction.ASC).unique());
  }
}
