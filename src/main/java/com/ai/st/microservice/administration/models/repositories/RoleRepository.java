package com.ai.st.microservice.administration.models.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.administration.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

}
