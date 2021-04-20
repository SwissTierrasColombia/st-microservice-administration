package com.ai.st.microservice.administration.dto;

import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerProfileDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel(value = "ManagerUserDto")
public class ManagerUserDto implements Serializable {

    @ApiModelProperty(required = true, notes = "User ID")
    private Long id;

    @ApiModelProperty(required = true, notes = "First Name")
    private String firstName;

    @ApiModelProperty(required = true, notes = "Last Name")
    private String lastName;

    @ApiModelProperty(required = true, notes = "Email")
    private String email;

    @ApiModelProperty(required = true, notes = "Username")
    private String username;

    @ApiModelProperty(required = true, notes = "Password")
    private String password;

    @ApiModelProperty(required = true, notes = "Is the user enabled?")
    private Boolean enabled;

    @ApiModelProperty(required = true, notes = "Creation date")
    private Date createdAt;

    @ApiModelProperty(required = true, notes = "Update date")
    private Date updatedAt;

    @ApiModelProperty(required = true, notes = "Profiles")
    private List<MicroserviceManagerProfileDto> profiles;

    @ApiModelProperty(required = true, notes = "Manager")
    private MicroserviceManagerDto manager;

    public ManagerUserDto() {
        this.profiles = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MicroserviceManagerProfileDto> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<MicroserviceManagerProfileDto> profiles) {
        this.profiles = profiles;
    }

    public MicroserviceManagerDto getManager() {
        return manager;
    }

    public void setManager(MicroserviceManagerDto manager) {
        this.manager = manager;
    }
}
