package com.autonavi.poi.service;


import com.autonavi.poi.controller.model.Page;
import com.autonavi.poi.controller.model.SearchCondition;
import com.autonavi.poi.dao.CaseDao;
import com.autonavi.poi.domain.casex.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * @author zhongyi.sun
 * @since 2016/6/30
 */
@Service
@Slf4j
public class CaseService {


    @Resource
    private CaseDao caseDao;


    public boolean createCase(String projectName, String moduleName) {
        TestCase testCase = new TestCase();
        testCase.setProjectName(projectName);
        testCase.setModuleName(moduleName);
        testCase.setCaseDesc("ssss");

        try {
            caseDao.insert(testCase);
        } catch (DuplicateKeyException e) {
            log.error("创建用例失败",e);
            return false;
        }
        return true;
    }


    public boolean delCase(String caseId) {
        try {
//            caseDao.delete(caseId);
            caseDao.delete2(caseId);
        } catch (Exception e) {
            log.error("创建用例失败",e);
            return false;
        }
        return true;
    }

    public boolean updateCase(TestCase testCase) {
        try {
            caseDao.update(testCase);
        } catch (Exception e) {
            log.error("更新用例失败",e);
            return false;
        }
        return true;
    }


    public Page findPage(SearchCondition condition, int currentPage, int perPage) {
        Page<TestCase> page = new Page<>();
        page.setCurrentPage(currentPage);
        page.setFrom((currentPage - 1) * perPage + 1);
        long total = caseDao.count(condition);
        page.setTotal(total);
        page.setTo(Math.min(currentPage * perPage, (int) total));
        page.setLastPage((int) ((total - 1) / perPage + 1));
        List<TestCase> cases = caseDao.find(condition, currentPage, perPage);
        page.setPerPage(perPage);
        page.setData(cases);
        return page;
    }


    public TestCase findById(String id) {
        List<TestCase> cases=caseDao.findById(id);
        if(cases.size()>0)
            return cases.get(0);
        return null;
    }



}
