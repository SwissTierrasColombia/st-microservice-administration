package com.ai.st.microservice.administration.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.entities.schema.administration.RoleEntity;
import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.microservice.administration.dto.RoleDto;
import com.ai.st.microservice.administration.dto.UserDto;
import com.ai.st.microservice.administration.models.services.IUserService;

@Component
public class UserBusiness {

	@Autowired
	private IUserService userService;

	public UserDto getUserByUsername(String username) {

		UserDto userDto = null;

		UserEntity userEntity = userService.getUserByUsername(username);
		if (userEntity != null) {
			userDto = new UserDto();

			userDto.setId(userEntity.getId());
			userDto.setFirstName(userEntity.getFirstName());
			userDto.setLastName(userEntity.getLastName());
			userDto.setEmail(userEntity.getEmail());
			userDto.setUsername(userEntity.getUsername());
			userDto.setEnabled(userEntity.getEnabled());
			userDto.setCreatedAt(userEntity.getCreatedAt());
			userDto.setUpdatedAt(userEntity.getUpdatedAt());
			userDto.setPassword(userEntity.getPassword());

			if (userEntity.getRoles().size() > 0) {
				for (RoleEntity roleEntity : userEntity.getRoles()) {
					userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
				}
			}

		}

		return userDto;
	}

}
