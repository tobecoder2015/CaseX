package com.autonavi.poi.dao;

import com.autonavi.poi.domain.Group;
import com.autonavi.poi.domain.Module;
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
public class GroupDao {

    @Resource
    private MongoOperations mongoOperations;

    public void insert(Group group) {
        mongoOperations.insert(group);
    }


    public List<Group> findByName(String groupName) {
        Query query = new Query();
        query.addCriteria(where("groupName").is(groupName));
        return mongoOperations.find(query, Group.class);
    }



}
