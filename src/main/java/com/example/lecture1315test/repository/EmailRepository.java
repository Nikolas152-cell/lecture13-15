package com.example.lecture1315test.repository;

import com.example.lecture1315test.models.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends MongoRepository<Email, Long> {
    Email findById(String id);
}
