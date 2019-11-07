package com.ai.st.microservice.administration.models.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.entities.schema.administration.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	public UserEntity findByUsername(String username);

}
