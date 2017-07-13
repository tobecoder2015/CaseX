package com.autonavi.poi.controller;


import com.autonavi.poi.controller.model.Page;
import com.autonavi.poi.controller.model.Response;
import com.autonavi.poi.controller.model.SearchCondition;
import com.autonavi.poi.domain.casex.*;
import com.autonavi.poi.service.CaseService;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qingshan.wqs
 * @since 2016/9/19
 */
@Slf4j
@RestController
@RequestMapping("cases")
public class CasesController {

    @Resource
    private CaseService caseService;

    @RequestMapping(method = RequestMethod.GET)
    public Page getCaseList(@RequestParam(name = "projectName", required = false) String projectName,
                            @RequestParam(name = "moduleName", required = false) String moduleName,
                            @RequestParam(name = "groupName", required = false) String groupName,
                            @RequestParam(name = "caseAuthor", required = false) String caseAuthor,
                            @RequestParam(name = "caseLevel", defaultValue = "-1") int caseLevel,
                            @RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "per_page", defaultValue = "10") int perPage,
                            @RequestParam(name = "sort", required = false) String sort
    ) {
        SearchCondition condition = new SearchCondition();
        if (StringUtils.isNotEmpty(projectName)) {
            condition.setProjectName(projectName.trim());
        }
        if (StringUtils.isNotEmpty(moduleName)) {
            condition.setModuleName(moduleName.trim());
        }
        if (StringUtils.isNotEmpty(groupName)) {
            condition.setGroupName(groupName.trim());
        }

        if (StringUtils.isNotEmpty(caseAuthor)) {
            condition.setCaseAuthor(caseAuthor.trim());
        }

        condition.setCaseLevel(caseLevel);
        if (StringUtils.isNotEmpty(sort)) {
            String[] sortArray = sort.split("\\|");
            if (sortArray.length == 2) {
                condition.setSort(sortArray[0]);
                if ("desc".equalsIgnoreCase(sortArray[1])) {
                    condition.setAsc(false);
                } else {
                    condition.setAsc(true);
                }
            }
        }
        return caseService.findPage(condition, page, perPage);
    }


    @RequestMapping(value = "case", method = RequestMethod.GET)
    public TestCase find(@RequestParam(name = "id", required = true) String id) {
        return caseService.findById(id);
    }


    @RequestMapping(value = "del", method = RequestMethod.POST)
    public Response del(@RequestParam(name = "caseId", required = true) String caseId) {
        Response response=new Response();
        boolean ok= caseService.delCase(caseId);
        response.setSuccess(ok);
        return response;
    }



    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Response add(@RequestParam(name = "addCase", required = true) String addCase) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        Response response=new Response();
        try {
            JsonNode node=mapper.readTree(addCase);
            TestCase updatecase=null;
            CaseContent caseContent=null;
            VerifyContent verifyContent=null;
            Integer caseType=node.get("case_type").intValue();
            updatecase=mapper.readValue(addCase, TestCase.class);

            switch (caseType){
                case 0:
                    if(node.get("case_content")!=null) {
                        caseContent = mapper.readValue(node.get("case_content").toString(), ApiCaseContent.class);
                    }
                    if(node.get("verify_content")!=null)
                        verifyContent=mapper.readValue(node.get("verify_content").toString(), ApiVerifyContent.class);
                    break;
                case 1:
                    if(node.get("case_content")!=null)
                        caseContent=mapper.readValue(node.get("case_content").toString(), FlowCaseContent.class);
                    if(node.get("verify_content")!=null)
                        verifyContent=mapper.readValue(node.get("verify_content").toString(), FlowVerifyContent.class);
                    break;
                default:
                    break;
            }
            updatecase.setCaseContent(caseContent);
            updatecase.setVerifyContent(verifyContent);
            updatecase.setCreateTime(new Date());
            updatecase.setCaseAuthor("WQS");
            updatecase.setUpdateTime(new Date());
            boolean ok= caseService.updateCase(updatecase);
            response.setSuccess(ok);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response update(@RequestParam(name = "editCase", required = true) String editCase) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        Response response=new Response();
        try {
            JsonNode node=mapper.readTree(editCase);
            TestCase updatecase=null;
            CaseContent caseContent=null;
            VerifyContent verifyContent=null;
            Integer caseType=node.get("case_type").intValue();
            updatecase=mapper.readValue(editCase, TestCase.class);

            switch (caseType){
                case 0:
                    if(node.get("case_content")!=null) {
                        caseContent = mapper.readValue(node.get("case_content").toString(), ApiCaseContent.class);
                    }
                    if(node.get("verify_content")!=null)
                        verifyContent=mapper.readValue(node.get("verify_content").toString(), ApiVerifyContent.class);
                    break;
                case 1:
                    if(node.get("case_content")!=null)
                        caseContent=mapper.readValue(node.get("case_content").toString(), FlowCaseContent.class);
                    if(node.get("verify_content")!=null)
                        verifyContent=mapper.readValue(node.get("verify_content").toString(), FlowVerifyContent.class);
                    break;
                default:
                    break;
            }
            updatecase.setCaseContent(caseContent);
            updatecase.setVerifyContent(verifyContent);

            updatecase.setUpdateTime(new Date());
            boolean ok= caseService.updateCase(updatecase);
            response.setSuccess(ok);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }


}
