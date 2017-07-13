package com.autonavi.poi.dao.mongo;

import com.autonavi.poi.domain.casex.TestCase;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface CaseDataRepository extends MongoRepository<TestCase,ObjectId> {


    @Query("{ 'projectName':?0,'moduleName':?1},$orderby:{id,-1}")
    List<TestCase> findCases(String projectName, String moduleName);
}
