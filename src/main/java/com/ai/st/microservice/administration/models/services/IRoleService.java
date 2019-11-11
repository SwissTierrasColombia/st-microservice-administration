package com.ai.st.microservice.administration.models.services;

import com.ai.st.entities.schema.administration.RoleEntity;

public interface IRoleService {

	public Long getAllCount();

	public RoleEntity createRole(RoleEntity role);

	public RoleEntity getRoleById(Long id);

}
