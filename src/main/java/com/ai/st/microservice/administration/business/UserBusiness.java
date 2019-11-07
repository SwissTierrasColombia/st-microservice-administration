package com.ai.st.microservice.administration.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.microservice.administration.models.services.IUserService;

@Component
public class UserBusiness {

	@Autowired
	private IUserService userService;

	public UserEntity getUserByUsername(String username) {
		UserEntity user = userService.getUserByUsername(username);
		return user;
	}

}
