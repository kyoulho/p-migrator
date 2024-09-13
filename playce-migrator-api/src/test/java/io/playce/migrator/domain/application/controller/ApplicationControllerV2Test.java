package io.playce.migrator.domain.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerV2Test {

    @Autowired
    ApplicationController applicationController;
//    @Autowired
//    ApplicationControllerV2 applicationControllerV2;

    StopWatch stopWatch = new StopWatch();

    @Test
    void watch(){
        long projectId = 1L;

        stopWatch.start("V1");
        applicationController.getApplications(projectId);
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());

//        stopWatch.start("V2");
//        applicationControllerV2.getApplications(projectId);
//        stopWatch.stop();
//        System.out.println(stopWatch.getLastTaskTimeMillis());
    }

}