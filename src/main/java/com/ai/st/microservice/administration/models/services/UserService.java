package com.ai.st.microservice.administration.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.microservice.administration.models.repositories.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserEntity getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
