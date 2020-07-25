package com.aman.userdata.userdatams.model;

import com.aman.userdata.userdatams.util.UserDataValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserDataOutputModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("page")
    private PageModel page = null;

    @JsonProperty("items")
    @Valid
    private List<UserData> items = new ArrayList<UserData>();

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }

    public List<UserData> getItems() {
        return items;
    }

    public void setItems(List<UserData> items) {
        this.items = items;
    }
}

