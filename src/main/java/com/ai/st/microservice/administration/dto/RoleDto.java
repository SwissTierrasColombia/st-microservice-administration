package com.ai.st.microservice.administration.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "RoleDto", description = "Role")
public class RoleDto implements Serializable {

    private static final long serialVersionUID = -2561206732349260810L;

    @ApiModelProperty(required = true, notes = "Role ID")
    private Long id;

    @ApiModelProperty(required = true, notes = "Role name")
    private String name;

    public RoleDto() {

    }

    public RoleDto(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
