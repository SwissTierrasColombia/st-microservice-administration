package com.ai.st.microservice.administration.models.services;

import com.ai.st.entities.schema.administration.UserEntity;

public interface IUserService {

	public UserEntity getUserByUsername(String username);

}
