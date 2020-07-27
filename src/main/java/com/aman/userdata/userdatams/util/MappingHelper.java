package com.aman.userdata.userdatams.util;

import com.aman.userdata.userdatams.model.UserData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MappingHelper {
    public UserData map(UserData in) {
        in.setLastPurchaseDateStr(new SimpleDateFormat(Constants.DATE_FORMAT).format(in.getLastPurchaseDate()));
        return in;
    }
    public JSONObject getResponseFirstObject(String reponseString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(reponseString);
        JSONArray jsonArray = (JSONArray) json.get("items");
        JSONObject responseFirstObject = (JSONObject) jsonArray.get(0);
        return responseFirstObject;
    }
}
