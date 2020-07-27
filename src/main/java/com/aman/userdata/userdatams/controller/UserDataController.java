package com.aman.userdata.userdatams.controller;

import com.aman.userdata.userdatams.model.UserDataOutputModel;
import com.aman.userdata.userdatams.service.UserDataService;
import com.aman.userdata.userdatams.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserDataController {

    @Autowired
    UserDataService userDataService;
    private static final Logger log = LogManager.getLogger(UserDataController.class);

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @RequestMapping(value = "/sales/record",
            produces = { "application/json" },
            consumes = { "application/text" },
            method = RequestMethod.POST)
    public ResponseEntity postApplications(
            @RequestBody String requestData) throws JsonProcessingException {
        log.info("Request received for processing the data.");
        log.debug("Recieved Data Request: \n" + requestData);
        try {
            userDataService.saveUser(requestData);
        }catch (Exception exception){
            log.error("Exception occurred while processing the request: Error Message: " + exception.getMessage(), exception);
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(mapper.writeValueAsString(exception.getMessage()));
        }
        log.info("Request processed successfully.");
        return ResponseEntity.status(HttpStatus.OK).body("Complete set processed successfully.");
    }
    @RequestMapping(value = "/sales/record",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity getUserData(
            @RequestParam(required = false) String purchaseDateFrom, @RequestParam(required = false) String purchaseDateTo,
            @RequestParam(required = false) String userName, @RequestParam(required = false) String age,
            @RequestParam(required = false) String saleAmount, @RequestParam(required = false, value = "0") Integer page,
            @RequestParam(required = false, value = "10") Integer limit, @RequestParam(required = false) String height,
            @RequestParam(required = false) String gender) throws JsonProcessingException {
        log.info("Request received for getting the User Data.");
        UserDataOutputModel userDataOutputModel = null;
        String jsonResponse = "";
        if (limit == null){
            limit = Constants.PARAM_LIMIT_DEF;
        }else if (limit > Constants.PARAM_LIMIT_MAX) {
            limit = Constants.PARAM_LIMIT_MAX;
        } else if (limit < Constants.PARAM_LIMIT_MIN) {
            limit = Constants.PARAM_LIMIT_MIN;
        }
        if (page == null || page < Constants.PARAM_PAGE_MIN) {
            page = Constants.PARAM_PAGE_MIN;
        }
        try {
            userDataOutputModel = userDataService.searchUser(userName, age, saleAmount, purchaseDateFrom,
                    purchaseDateTo, height, gender, page, limit);
        }catch (Exception exception){
            log.error("Exception occurred while processing the request: Error Message: " + exception.getMessage(), exception);
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(mapper.writeValueAsString(exception.getMessage()));
        }
        jsonResponse = getJsonResponse(userDataOutputModel);
        log.info("Request processed successfully. Sending Response to Requester.");
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    }
    private String getJsonResponse(UserDataOutputModel userDataOutputModel) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(userDataOutputModel);
    }
}
