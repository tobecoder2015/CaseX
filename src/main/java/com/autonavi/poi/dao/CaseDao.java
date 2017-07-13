package com.autonavi.poi.dao;

import com.autonavi.poi.controller.model.Page;
import com.autonavi.poi.controller.model.SearchCondition;
import com.autonavi.poi.domain.casex.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by qunle.dql on 2016/12/7.
 */
@Repository
public class CaseDao {

    @Resource
    private MongoOperations mongoOperations;

    public void insert(TestCase apiCase) {
        mongoOperations.insert(apiCase);
    }
    public void delete(String caseId) {
        Query query = new Query();
        if (StringUtils.isNotEmpty(caseId)) {
            query.addCriteria(where("id").is(caseId));
            mongoOperations.remove(query,TestCase.class);
        }
    }

    public void delete2(String caseId) {
        if (StringUtils.isNotEmpty(caseId)) {
            Query query = new Query();
            query.addCriteria(where("id").is(caseId));
            Update update=new Update();
            update.set("deleteFlag",1);
            mongoOperations.updateFirst(query,update,TestCase.class);
        }
    }


    public void update(TestCase testCase) {
        mongoOperations.save(testCase);
//        Query query = new Query();
//        if (testCase==null) {
//           return;
//        }
//        Update update=new Update();
//        update.addToSet("id",testCase);
//        mongoOperations.updateFirst(query,update,TestCase.class);
    }



    public long count(SearchCondition condition) {
        Query query = getSearchQuery(condition);
        return mongoOperations.count(query, TestCase.class);
    }



    private Query getSearchQuery(SearchCondition condition) {
        Query query = new Query();
        query.addCriteria(where("deleteFlag").is(condition.getDeleteFlag()));
        if (StringUtils.isNotEmpty(condition.getProjectName())) {
            query.addCriteria(where("projectName").regex(Pattern.quote(condition.getProjectName())));
        }
        if (StringUtils.isNotEmpty(condition.getModuleName())) {
            query.addCriteria(where("moduleName").regex(Pattern.quote(condition.getModuleName())));
        }
        if (StringUtils.isNotEmpty(condition.getGroupName())) {
            query.addCriteria(where("groupName").regex(Pattern.quote(condition.getGroupName())));
        }

        if (StringUtils.isNotEmpty(condition.getCaseAuthor())) {
            query.addCriteria(where("caseAuthor").regex(Pattern.quote(condition.getCaseAuthor())));
        }
        if (condition.getCaseLevel() > -1) {
            query.addCriteria(where("caseLevel").is(condition.getCaseLevel()));
        }
        return query;
    }

    public List<TestCase> find(SearchCondition condition, int currentPage, int perPage) {
        Query query = getSearchQuery(condition);
        if (StringUtils.isNotEmpty(condition.getSort())) {
            query.with(new Sort(condition.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, condition.getSort()));
        }
        query.skip((currentPage - 1) * perPage).limit(perPage);
        return mongoOperations.find(query, TestCase.class);
    }
//    public void update(String taskId,String ocr_text,String pic,String polygon,int is_poi){
//        Update update = new Update();
//        update.set("is_poi", is_poi);
//        mongoOperations.updateFirst(query(where("taskId").is(taskId)
//        .and("ocr_text").is(ocr_text)
//        .and("pic").is(pic)
//        .and("polygon").is(polygon)), update, PoiDetail.class);
//    }
//
//    public PoiDetail findByMulCon(String taskId,String ocr_text,String pic,String polygon) {
//        Query query = new Query();
//        query.addCriteria(where("taskId").is(taskId)
//                .and("ocr_text").is(ocr_text)
//                .and("pic").is(pic)
//                .and("polygon").is(polygon));
//        return mongoOperations.findOne(query, PoiDetail.class);
//    }

    public List<TestCase> findById(String id) {
        Query query = new Query();
        query.addCriteria(where("id").is(id));
        return mongoOperations.find(query, TestCase.class);
    }


    public List<TestCase> findByGroupName(String groupName) {
        Query query = new Query();
        query.addCriteria(where("groupName").is(groupName));
        return mongoOperations.find(query, TestCase.class);
    }



    public List<TestCase> findByModuleName(String moduleName) {
        Query query = new Query();
        query.addCriteria(where("moduleName").is(moduleName));
        return mongoOperations.find(query, TestCase.class);
    }


    public List<TestCase> findByProjectName(String projectName) {
        Query query = new Query();
        query.addCriteria(where("projectName").is(projectName));
        return mongoOperations.find(query, TestCase.class);
    }



}
