package com.ai.st.microservice.administration.models.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ai.st.microservice.administration.entities.CodeEntity;
import com.ai.st.microservice.administration.entities.UserEntity;

public interface CodeRepository extends CrudRepository<CodeEntity, Long> {

	@Modifying
	@Query("update CodeEntity c set c.available = false where c.user.id = :userId")
	public void unavailableCodesByUser(@Param(value = "userId") Long userId);

	public CodeEntity findByCodeAndUser(String code, UserEntity user);

}
