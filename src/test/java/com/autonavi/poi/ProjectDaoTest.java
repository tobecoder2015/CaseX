package com.autonavi.poi;

import com.autonavi.poi.dao.ProjectDao;
import com.autonavi.poi.domain.Project;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by qingshan.wqs on 2016/11/24.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DemoApplication.class)
public class ProjectDaoTest {

    @Resource
    ProjectDao projectDao;


    @org.junit.Test
    public void test2() throws Exception {

        Project project=new Project();
        project.setOwner("wqs");
        project.setProjectName("CaseX");
        project.setProjectDesc("Case Manager");
        projectDao.insert(project);
    }

}
