package com.ai.st.microservice.administration.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.administration.entities.RoleEntity;
import com.ai.st.microservice.administration.models.repositories.RoleRepository;

@Service
public class RoleService implements IRoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Long getAllCount() {
		return roleRepository.count();
	}

	@Override
	public RoleEntity createRole(RoleEntity role) {
		return roleRepository.save(role);
	}

	@Override
	public RoleEntity getRoleById(Long id) {
		return roleRepository.findById(id).orElse(null);
	}

}
