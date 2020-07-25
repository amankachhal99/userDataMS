package com.aman.userdata.userdatams.util;

import com.aman.userdata.userdatams.model.UserData;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserDataValidator implements Validator {

    /**
     * This Validator validates *just* Person instances
     */
    public boolean supports(Class clazz) {
        return UserData.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "userName", "Name should not be null");
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "age", "Age should not be null");
        UserData p = (UserData) obj;
        if (p.getAge() < 0) {
            e.rejectValue("age", "Age should be a positive number");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "height", "Height should not be null");
        if (p.getHeight() < 0) {
            e.rejectValue("height", "Height should be a positive number");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "gender", "Gender should not be null");
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "saleAmount", "Amount should not be null");
        if (p.getSaleAmount() < 0) {
            e.rejectValue("saleAmount", "Amount should be a positive number");
        }
        if(!isValidDate(p.getLastPurchaseDateStr())){
            e.rejectValue("lastPurchaseDate", "Last Purchase Date should be in format " + Constants.DATE_FORMAT);
        }
    }
    private boolean isValidDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}