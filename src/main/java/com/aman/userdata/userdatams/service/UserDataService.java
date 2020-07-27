package com.aman.userdata.userdatams.service;

import com.aman.userdata.userdatams.model.PageModel;
import com.aman.userdata.userdatams.model.UserData;
import com.aman.userdata.userdatams.model.UserDataOutputModel;
import com.aman.userdata.userdatams.repository.JPAUserDataRepository;
import com.aman.userdata.userdatams.util.Constants;
import com.aman.userdata.userdatams.util.MappingHelper;
import com.aman.userdata.userdatams.util.UserDataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDataService {
    private static final Logger log = LogManager.getLogger(UserDataService.class);
    JPAUserDataRepository jpaUserDataRepository;
    private MappingHelper mappingHelper;

    public UserDataService(JPAUserDataRepository jpaUserDataRepository, MappingHelper mappingHelper) {
        this.jpaUserDataRepository = jpaUserDataRepository;
        this.mappingHelper = mappingHelper;
    }

    /*
        This method will be responsible to process, validate and persist the data.
    */
    public void saveUser(BufferedReader requestData) throws Exception {
        String line="";
        StringBuilder dataLine= new StringBuilder();
        log.info("Starting to process the data.");
        while( null != (line = requestData.readLine() ) ) {
            dataLine.append(line).append("\n");
        }
        saveUser(dataLine.toString());
    }

    public void saveUser(String requestData) throws Exception {
        String[] dataLine = requestData.split("\n");
        //Step #1: Spilt the data
        List<UserData> userDataList = new ArrayList<UserData>();
        List<String> errorList = new ArrayList<String>();
        //Step #2: Process & validate the data
        int count=0;
        for (String data : dataLine) {
            try {
                UserData userData = modelUserData(data.split(Constants.CSV_SEPERATOR), count);
                if (userData != null) {
                    userDataList.add(userData);
                }
            }catch (Exception excep){
                errorList.add(excep.getMessage());
            }
            count++;
        }
        if(errorList.size() > 0){
            throw new Exception(errorList.toString());
        }
        //Step #3: Persist the data
        jpaUserDataRepository.saveAll(userDataList);
    }
    public UserDataOutputModel searchUser(String userName, String age, String saleAmount, String purchaseDateFrom,
                                          String purchaseDateTo,String height, String gender, int page, int limit){
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<UserData> userDataPage = jpaUserDataRepository.findByCriteria(userName != null ? userName.toUpperCase() : null, age, saleAmount,
                purchaseDateFrom, purchaseDateTo, height, gender != null ? gender.toUpperCase() : null, pageRequest);

        int actualCurrentPage = page;
        if (actualCurrentPage > 0 && actualCurrentPage > userDataPage.getTotalPages() - 1) {
            actualCurrentPage = userDataPage.getTotalPages() - 1;
            // Retrieve record for the actual last page if asking page number exceeded
            pageRequest = PageRequest.of(actualCurrentPage, limit);
            userDataPage = jpaUserDataRepository.findByCriteria(userName != null ? userName.toUpperCase() : null, age, saleAmount,
                    purchaseDateFrom, purchaseDateTo, height, gender != null ? gender.toUpperCase() : null, pageRequest);
        }

        UserDataOutputModel userDataOutputModel = new UserDataOutputModel();
        PageModel pageModel = new PageModel();
        pageModel.setCurrent(actualCurrentPage);
        pageModel.setTotalItems((int)userDataPage.getTotalElements());
        pageModel.setSize(limit);
        userDataOutputModel.setPage(pageModel);
        userDataOutputModel.setItems(userDataPage.getContent().stream().map(mappingHelper::map).collect(Collectors.toList()));
        return userDataOutputModel;
    }


    /*
        This method will be responsible to process, validate the data.
     */
    private UserData modelUserData(String [] dataArray, int rowCount) throws Exception{
        List<String> errorMessages = new ArrayList<String>();
        UserData userData = new UserData();
        try {
            if (dataArray[0] != null && !StringUtils.isEmpty(dataArray[0]) && "USER_NAME".equalsIgnoreCase(dataArray[0])){
                return null;
            }
            userData.setUserName(dataArray[0]);
            userData.setAge(Integer.parseInt(dataArray[1]));
            userData.setHeight(Integer.parseInt(dataArray[2]));
            userData.setGender(dataArray[3]);
            userData.setSaleAmount(Float.parseFloat(dataArray[4]));
            userData.setLastPurchaseDateStr(dataArray[5].trim());
        }catch (Exception exception){
            errorMessages.add("Error while processing row# " + (rowCount + 1) + " Error: " + exception.getMessage());
            log.error("Error while processing row# " + (rowCount + 1) + " Error: " + exception.getMessage(), exception);
            throw new Exception(errorMessages.toString());
        }
        UserDataValidator userDataValidator = new UserDataValidator();
        Errors errors = new BeanPropertyBindingResult(userData, "userData");
        userDataValidator.validate(userData, errors);
        if (errors.hasErrors()) {
            for (int errorCount = 0; errorCount < errors.getAllErrors().size(); errorCount++) {
                Object rejectedValueObj = ((FieldError) errors.getAllErrors().get(errorCount)).getRejectedValue();
                String givenValue = rejectedValueObj != null && rejectedValueObj.toString() != "" ? " Given value: " + rejectedValueObj.toString() : "";
                errorMessages.add("Error while processing row# " + (rowCount + 1) + givenValue + " Error: " + errors.getAllErrors().get(errorCount).getCode());
                log.error("Error while processing row# " + (rowCount + 1) + givenValue + " Error: " + errors.getAllErrors().get(errorCount).getCode());
            }
            throw new Exception(errorMessages.toString());
        }
        userData.setLastPurchaseDate(new SimpleDateFormat(Constants.DATE_FORMAT).parse(dataArray[5].trim()));
        return userData;
    }
}
