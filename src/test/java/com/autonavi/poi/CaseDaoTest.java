package com.autonavi.poi;

import com.autonavi.poi.controller.model.SearchCondition;
import com.autonavi.poi.dao.CaseDao;
import com.autonavi.poi.domain.casex.ApiCaseContent;
import com.autonavi.poi.domain.casex.FlowCaseContent;
import com.autonavi.poi.domain.casex.TestCase;
import com.autonavi.poi.domain.contant.CaseTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by qingshan.wqs on 2016/11/24.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DemoApplication.class)
public class CaseDaoTest {

    @Resource
    CaseDao caseDao;

    @Resource
    MongoOperations mongoOperations;

    @Autowired
    ObjectMapper mapper;

    @org.junit.Test
    public void test2() throws Exception {

            ApiCaseContent caseContent = new ApiCaseContent();
            caseContent.setPath("/api");
        Map paraMap=new HashMap();
        paraMap.put("tracefile","trace.zip");
        paraMap.put("frameFile","北京.zip");
        caseContent.setParaMap(mapper.writeValueAsString(paraMap));
            TestCase testCase = new TestCase();
            testCase.setCaseContent(caseContent);
            testCase.setProjectName("冰河");
        testCase.setModuleName("高精车");
        testCase.setGroupName("高精车申请任务");
        testCase.setCaseName("选择路径");
        testCase.setCaseDesc("选择路径，对对应路径形成任务");
            testCase.setCaseAuthor("lyy");
            caseDao.insert(testCase);


        FlowCaseContent flowCaseContent = new FlowCaseContent();
        flowCaseContent.setMethod("apply");


         testCase = new TestCase();
        testCase.setCaseContent(flowCaseContent);
        testCase.setCaseType(CaseTypeEnum.流程用例.value());
        testCase.setProjectName("冰河");
        testCase.setModuleName("高精车");
        testCase.setGroupName("高精车申请任务");
        testCase.setCaseName("选择路径");
        testCase.setCaseDesc("选择路径，对对应路径形成任务");
        testCase.setCaseAuthor("lyy");
        caseDao.insert(testCase);

    }

    @org.junit.Test
    public void test1() throws Exception {
       List<TestCase> cases= caseDao.findById("587b714fee0d160574d6fd66");
        log.info(cases.toString());
    }


    @org.junit.Test
    public void test3() throws Exception {
        List<TestCase> cases= caseDao.findByGroupName("自行车申请任务");
        log.info(cases.toString());
    }


    @org.junit.Test
    public void test4() throws Exception {
        List<TestCase> cases= caseDao.findByModuleName("自行车");
        log.info(cases.toString());
    }

    @org.junit.Test
    public void test5() throws Exception {
        SearchCondition condition=new SearchCondition();
        condition.setCaseLevel(-1);
        Query query = getSearchQuery(condition);
        if (StringUtils.isNotEmpty(condition.getSort())) {
            query.with(new Sort(condition.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, condition.getSort()));
        }
        query.skip(0).limit(20);
        List<TestCase> cases= mongoOperations.find(query, TestCase.class);
        log.info(cases.toString());
    }

    private Query getSearchQuery(SearchCondition condition) {
        Query query = new Query();
        if (StringUtils.isNotEmpty(condition.getProjectName())) {
            query.addCriteria(where("projectName").regex(Pattern.quote(condition.getProjectName())));
        }
        if (StringUtils.isNotEmpty(condition.getModuleName())) {
            query.addCriteria(where("moduleName").regex(Pattern.quote(condition.getModuleName())));
        }
        if (StringUtils.isNotEmpty(condition.getGroupName())) {
            query.addCriteria(where("groupName").regex(Pattern.quote(condition.getGroupName())));
        }
        if (condition.getCaseLevel() > -1) {
            query.addCriteria(where("caseLevel").is(condition.getCaseLevel()));
        }
        return query;
    }




}
