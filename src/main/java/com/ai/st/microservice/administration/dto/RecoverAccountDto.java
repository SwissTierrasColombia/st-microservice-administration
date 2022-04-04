package com.ai.st.microservice.administration.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "RecoverAccountDto", description = "Recover Account Dto")
public class RecoverAccountDto implements Serializable {

    private static final long serialVersionUID = 6919516948250085614L;

    @ApiModelProperty(required = true, notes = "Email")
    private String email;

    public RecoverAccountDto() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
