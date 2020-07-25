package com.aman.userdata.userdatams.controller;

import com.aman.userdata.userdatams.model.UserData;
import com.aman.userdata.userdatams.repository.JPAUserDataRepository;
import com.aman.userdata.userdatams.service.UserDataService;
import com.aman.userdata.userdatams.util.MappingHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserDataControllerTest {
    private UserDataController userDataController;
    @Mock
    JPAUserDataRepository jpaUserDataRepository;
    @Mock
    MappingHelper mappingHelper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        userDataController = new UserDataController(new UserDataService(jpaUserDataRepository, mappingHelper));
    }

    /*
        To Validate the successful flow of the POST /sales/record
     */
    @Test
    public void postApplicationsTest() throws JsonProcessingException {
        String userTestInput ="John Doeg,29,177,M,21312,2020-11-05T13:15:30Z";
        UserData userData = new UserData("John Doeg",29,177,"M",Float.valueOf("312")
                ,"2020-11-05T13:15:30Z");
        List<UserData> userDataList = new ArrayList<UserData>();
        userDataList.add(userData);
        when(jpaUserDataRepository.saveAll(any())).thenReturn(userDataList);
        ResponseEntity response = userDataController.postApplications(userTestInput);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    /*
        To Validate that the following validation criteria are working for POST /sales/record
        1. Age should be a positive number
     */
    @Test
    public void postApplicationsTestNegitiveAge() throws JsonProcessingException {
        String userTestInput ="John Doeg,-29,177,M,21312,2020-11-05T13:15:30Z";
        ResponseEntity response = userDataController.postApplications(userTestInput);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Assert.assertEquals("\"[[Error while processing row# 1 Given value: -29 Error: Age should be a positive number]]\"",
                response.getBody());
    }
    /*
        To Validate that the following validation criteria are working for POST /sales/record
        1. Height should be a positive number
    */
    @Test
    public void postApplicationsTestNegitiveHeight() throws JsonProcessingException {
        String userTestInput ="John Doeg,29,-177,M,21312,2020-11-05T13:15:30Z";
        ResponseEntity response = userDataController.postApplications(userTestInput);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Assert.assertEquals("\"[[Error while processing row# 1 Given value: -177 Error: Height should be a positive number]]\"",
                response.getBody());
    }
    /*
        To Validate that the following validation criteria are working for POST /sales/record
        1. Sale Amount should be a positive number
    */
    @Test
    public void postApplicationsTestNegitiveSaleAmount() throws JsonProcessingException {
        String userTestInput ="John Doeg,29,177,M,-312,2020-11-05T13:15:30Z";
        ResponseEntity response = userDataController.postApplications(userTestInput);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Assert.assertEquals("\"[[Error while processing row# 1 Given value: -312.0 Error: Amount should be a positive number]]\"",
                response.getBody());
    }
    /*
        To Validate that the following validation criteria are working for POST /sales/record
        1. Last Purchase Date should be in correct format
    */
    @Test
    public void postApplicationsTestPurchaseDateValid() throws JsonProcessingException {
        String userTestInput ="John Doeg,29,177,M,312,2020-23-11";
        ResponseEntity response = userDataController.postApplications(userTestInput);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Assert.assertEquals("\"[[Error while processing row# 1 Error: Last Purchase Date should be in format yyyy-MM-dd'T'HH:mm:ss]]\"",
                response.getBody());
    }
    /*
        To Validate that the following validation criteria are working for POST /sales/record
        1. User Name should not be blank
    */
    @Test
    public void postApplicationsTestBlankUserName() throws JsonProcessingException {
        String userTestInput =",29,177,M,21312,2020-11-05T13:15:30Z";
        ResponseEntity response = userDataController.postApplications(userTestInput);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Assert.assertEquals("\"[[Error while processing row# 1 Given value:  Error: Name should not be null]]\"",
                response.getBody());
    }
    /*
        To Validate that the following validation criteria are working for POST /sales/record
        1. Gender should not be blank
    */
    @Test
    public void postApplicationsTestBlankGender() throws JsonProcessingException {
        String userTestInput ="John,29,177,,21312,2020-11-05T13:15:30Z";
        ResponseEntity response = userDataController.postApplications(userTestInput);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        Assert.assertEquals("\"[[Error while processing row# 1 Given value:  Error: Gender should not be null]]\"",
                response.getBody());
    }
}