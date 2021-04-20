package com.ai.st.microservice.administration.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UpdateUserDto", description = "Create User Dto")
public class UpdateUserDto implements Serializable {

    private static final long serialVersionUID = -2041172889834251468L;

    @ApiModelProperty(required = true, notes = "First name")
    private String firstName;

    @ApiModelProperty(required = true, notes = "Last name")
    private String lastName;

    @ApiModelProperty(required = true, notes = "Email")
    private String email;

    public UpdateUserDto() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
