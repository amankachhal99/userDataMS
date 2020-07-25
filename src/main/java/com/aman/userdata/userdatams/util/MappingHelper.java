package com.aman.userdata.userdatams.util;

import com.aman.userdata.userdatams.model.UserData;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MappingHelper {
    public UserData map(UserData in) {
        in.setLastPurchaseDateStr(new SimpleDateFormat(Constants.DATE_FORMAT).format(in.getLastPurchaseDate()));
        return in;
    }
}
