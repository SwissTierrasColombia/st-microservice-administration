package com.ai.st.microservice.administration.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ChangePasswordDto", description = "Change Password Dto")
public class ChangePasswordDto implements Serializable {

    private static final long serialVersionUID = 6445260471019404726L;

    @ApiModelProperty(required = true, notes = "Password")
    private String password;

    public ChangePasswordDto() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
