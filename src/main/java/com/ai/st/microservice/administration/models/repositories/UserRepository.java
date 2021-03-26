package com.ai.st.microservice.administration.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ai.st.microservice.administration.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);

	UserEntity findByEmail(String email);

	@Query(nativeQuery = true, value = "SELECT u.* FROM administration.users u JOIN administration.users_x_roles ur ON u.id = ur.user_id AND ur.role_id IN :roles")
	List<UserEntity> getUserByRoles(@Param("roles") List<Long> roles);

	@Override
	List<UserEntity> findAll();

}
