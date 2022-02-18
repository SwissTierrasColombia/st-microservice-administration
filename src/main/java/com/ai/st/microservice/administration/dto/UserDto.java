package com.ai.st.microservice.administration.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UserDto", description = "User")
public class UserDto implements Serializable {

	private static final long serialVersionUID = -5121529899322990688L;

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

	@ApiModelProperty(required = true, notes = "Amount of successful logins")
	private Integer amountSuccessfulLogins;

	@ApiModelProperty(required = true, notes = "Creation date")
	private Date createdAt;

	@ApiModelProperty(required = true, notes = "Last login")
	private Date lastLogin;

	@ApiModelProperty(required = true, notes = "Update date")
	private Date updatedAt;

	@ApiModelProperty(required = false, notes = "Roles user")
	private List<RoleDto> roles;

	public UserDto() {
		this.roles = new ArrayList<RoleDto>();
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

	public List<RoleDto> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDto> roles) {
		this.roles = roles;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getAmountSuccessfulLogins() {
		return amountSuccessfulLogins;
	}

	public void setAmountSuccessfulLogins(Integer amountSuccessfulLogins) {
		this.amountSuccessfulLogins = amountSuccessfulLogins;
	}
}
