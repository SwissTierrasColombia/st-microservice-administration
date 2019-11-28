package com.ai.st.microservice.administration.models.services;

import com.ai.st.microservice.administration.entities.UserEntity;

public interface IUserService {

	public UserEntity getUserByUsername(String username);

	public UserEntity getUserById(Long id);

	public Long getAllCount();

	public UserEntity createUser(UserEntity user);

	public UserEntity getUserByEmail(String email);

}
