package com.ai.st.microservice.administration.models.services;

import java.util.List;

import com.ai.st.microservice.administration.entities.UserEntity;

public interface IUserService {

	UserEntity getUserByUsername(String username);

	UserEntity getUserById(Long id);

	Long getAllCount();

	UserEntity createUser(UserEntity user);

	List<UserEntity> getUsersByEmail(String email);

	List<UserEntity> getAllUsers();

	List<UserEntity> getUserByRoles(List<Long> roles);

}
