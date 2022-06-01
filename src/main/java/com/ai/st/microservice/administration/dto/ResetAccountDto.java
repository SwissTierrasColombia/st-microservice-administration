package com.ai.st.microservice.administration.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ResetAccountDto")
public class ResetAccountDto implements Serializable {

    private static final long serialVersionUID = 6157910153982446915L;

    @ApiModelProperty(required = true, notes = "Email")
    private String username;

    @ApiModelProperty(required = true, notes = "Code (OTP)")
    private String code;

    @ApiModelProperty(required = true, notes = "New Password")
    private String newPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetAccountDto{" + "username='" + username + '\'' + ", code='" + code + '\'' + ", newPassword='"
                + newPassword + '\'' + '}';
    }
}
