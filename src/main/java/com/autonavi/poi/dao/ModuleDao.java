package com.autonavi.poi.dao;

import com.autonavi.poi.domain.Module;
import com.autonavi.poi.domain.Project;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by qunle.dql on 2016/12/7.
 */
@Repository
public class ModuleDao {

    @Resource
    private MongoOperations mongoOperations;

    public void insert(Module module) {
        mongoOperations.insert(module);
    }


    public List<Module> findByName(String moduleName) {
        Query query = new Query();
        query.addCriteria(where("moduleName").is(moduleName));
        return mongoOperations.find(query, Module.class);
    }



}
