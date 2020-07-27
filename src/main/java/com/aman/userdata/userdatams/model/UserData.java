package com.aman.userdata.userdatams.model;

import com.aman.userdata.userdatams.util.Constants;
import com.aman.userdata.userdatams.util.UserDataValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "USER_DATA")
@Validated(value= UserDataValidator.class)
public class UserData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable=false, columnDefinition="int")
    @JsonIgnore
    private Integer userId;

    @Column(name = "USER_NAME", columnDefinition = "varchar(200)")
    @JsonProperty("userName")
    @NotBlank (message = "User_Name value cannot be blank")
    @NotNull (message = "User_Name value cannot be null")
    private String userName;

    @Column(name = "AGE", columnDefinition = "int")
    @JsonProperty("age")
    @Positive(message = "Age should be a positive number")
    @NotNull (message = "Age value cannot be null")
    private Integer age;

    @Column(name = "HEIGHT", columnDefinition = "int")
    @JsonProperty("height")
    @Positive(message = "Height should be a positive number")
    @NotNull (message = "Height value cannot be null")
    private Integer height;

    @Column(name = "GENDER", columnDefinition = "char(1)")
    @JsonProperty("gender")
    @NotBlank (message = "Gender value cannot be blank")
    @NotNull (message = "Gender value cannot be null")
    private String gender;

    @Column(name = "SALE_AMOUNT", columnDefinition = "float(10,2)")
    @JsonProperty("saleAmount")
    @Positive(message = "Sale_Amount should be a positive number")
    @NotNull (message = "Sale_Amount value cannot be null")
    private Float saleAmount;

    @Transient
    @JsonProperty("lastPurchaseDate")
    private String lastPurchaseDateStr;

    @Column(name = "LAST_PURCHASE_DATE", columnDefinition = "datetime")
    @JsonIgnore
    private Date lastPurchaseDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public UserData(){}

    public UserData(
            @NotBlank(message = "User_Name value cannot be blank") @NotNull(message = "User_Name value cannot be null") String userName,
            @Positive(message = "Age should be a positive number") @Max(value = 110, message = "Age cannot be greater than 110")
            @NotNull(message = "Age value cannot be null") Integer age,
            @Positive(message = "Height should be a positive number") @NotNull(message = "Height value cannot be null") Integer height,
            @NotBlank(message = "Gender value cannot be blank") @NotNull(message = "Gender value cannot be null") String gender,
            @Positive(message = "Sale_Amount should be a positive number") @NotNull(message = "Sale_Amount value cannot be null")
                    Float saleAmount, String lastPurchaseDateStr) throws ParseException {
        this.userName = userName;
        this.age = age;
        this.height = height;
        this.gender = gender;
        this.saleAmount = saleAmount;
        this.lastPurchaseDateStr = lastPurchaseDateStr;
        this.lastPurchaseDate = new SimpleDateFormat(Constants.DATE_FORMAT).parse(lastPurchaseDateStr);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Float getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Float saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getLastPurchaseDateStr() {
        return lastPurchaseDateStr;
    }

    public void setLastPurchaseDateStr(String lastPurchaseDateStr) {
        this.lastPurchaseDateStr = lastPurchaseDateStr;

    }

    public Date getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(Date lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }
}
