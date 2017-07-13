package com.autonavi.poi.dao;

import com.autonavi.poi.domain.Project;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by qunle.dql on 2016/12/7.
 */
@Repository
public class ProjectDao {

    @Resource
    private MongoOperations mongoOperations;

    public void insert(Project project) {
        mongoOperations.insert(project);
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

    public List<Project> findByName(String projectName) {
        Query query = new Query();
        query.addCriteria(where("projectName").is(projectName));
        return mongoOperations.find(query, Project.class);
    }



}
