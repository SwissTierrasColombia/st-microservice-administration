package com.ai.st.microservice.administration.models.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.administration.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	public UserEntity findByUsername(String username);

	public UserEntity findByEmail(String email);

	@Override
	public List<UserEntity> findAll();

}
