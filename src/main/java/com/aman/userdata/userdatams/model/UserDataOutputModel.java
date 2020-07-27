package com.aman.userdata.userdatams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDataOutputModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("page")
    private PageModel page = null;

    @JsonProperty("items")
    @Valid
    private List<UserData> items = new ArrayList<UserData>();

}

