package com.ai.st.microservice.administration.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.administration.dto.RoleDto;
import com.ai.st.microservice.administration.dto.UserDto;
import com.ai.st.microservice.administration.entities.RoleEntity;
import com.ai.st.microservice.administration.entities.UserEntity;
import com.ai.st.microservice.administration.exceptions.BusinessException;
import com.ai.st.microservice.administration.models.services.IRoleService;
import com.ai.st.microservice.administration.models.services.IUserService;

@Component
public class UserBusiness {

	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

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

	public UserDto getUserById(Long id) {

		UserDto userDto = null;

		UserEntity userEntity = userService.getUserById(id);
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

	public UserDto createUser(String firstName, String lastName, String password, String email, String username,
			List<Long> roles) throws BusinessException {

		email = email.toLowerCase().trim();
		username = username.toLowerCase().trim();

		// validate roles
		List<RoleEntity> listRoles = new ArrayList<RoleEntity>();
		roles = roles.stream().distinct().collect(Collectors.toList());
		for (Long roleId : roles) {
			RoleEntity roleEntity = roleService.getRoleById(roleId);
			if (!(roleEntity instanceof RoleEntity)) {
				throw new BusinessException("El rol no existe.");
			} else {
				listRoles.add(roleEntity);
			}
		}

		// validation username
		UserEntity userUsernameEntity = userService.getUserByUsername(username);
		if (userUsernameEntity instanceof UserEntity) {
			throw new BusinessException("Ya existe un usuario con el nombre de usuario especificado.");
		}

		if (password == null || password.isEmpty()) {
			password = RandomStringUtils.random(6, true, true);
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);
		userEntity.setCreatedAt(new Date());
		userEntity.setEmail(email);
		userEntity.setEnabled(true);
		userEntity.setUsername(username);
		userEntity.setPassword(passwordEncode.encode(password));
		userEntity.setRoles(listRoles);

		userEntity = userService.createUser(userEntity);

		UserDto userDto = new UserDto();

		userDto.setId(userEntity.getId());
		userDto.setFirstName(userEntity.getFirstName());
		userDto.setLastName(userEntity.getLastName());
		userDto.setEmail(userEntity.getEmail());
		userDto.setUsername(userEntity.getUsername());
		userDto.setEnabled(userEntity.getEnabled());
		userDto.setCreatedAt(userEntity.getCreatedAt());
		userDto.setUpdatedAt(userEntity.getUpdatedAt());
		userDto.setPassword(userEntity.getPassword());

		for (RoleEntity roleEntity : userEntity.getRoles()) {
			userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
		}

		return userDto;
	}

	public List<UserDto> getUsers() throws BusinessException {

		List<UserDto> listUsersDto = new ArrayList<UserDto>();

		List<UserEntity> listUsersEntity = userService.getAllUsers();

		for (UserEntity userEntity : listUsersEntity) {

			UserDto userDto = new UserDto();

			userDto.setId(userEntity.getId());
			userDto.setFirstName(userEntity.getFirstName());
			userDto.setLastName(userEntity.getLastName());
			userDto.setEmail(userEntity.getEmail());
			userDto.setUsername(userEntity.getUsername());
			userDto.setEnabled(userEntity.getEnabled());
			userDto.setCreatedAt(userEntity.getCreatedAt());
			userDto.setUpdatedAt(userEntity.getUpdatedAt());
			userDto.setPassword(null);

			if (userEntity.getRoles().size() > 0) {
				for (RoleEntity roleEntity : userEntity.getRoles()) {
					userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
				}
			}

			listUsersDto.add(userDto);
		}

		return listUsersDto;
	}

	public List<UserDto> getUsersByRoles(List<Long> roles) throws BusinessException {

		List<UserDto> listUsersDto = new ArrayList<UserDto>();

		List<UserEntity> listUsersEntity = userService.getUserByRoles(roles);

		for (UserEntity userEntity : listUsersEntity) {

			UserDto userDto = new UserDto();

			userDto.setId(userEntity.getId());
			userDto.setFirstName(userEntity.getFirstName());
			userDto.setLastName(userEntity.getLastName());
			userDto.setEmail(userEntity.getEmail());
			userDto.setUsername(userEntity.getUsername());
			userDto.setEnabled(userEntity.getEnabled());
			userDto.setCreatedAt(userEntity.getCreatedAt());
			userDto.setUpdatedAt(userEntity.getUpdatedAt());
			userDto.setPassword(null);

			if (userEntity.getRoles().size() > 0) {
				for (RoleEntity roleEntity : userEntity.getRoles()) {
					userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
				}
			}

			listUsersDto.add(userDto);
		}

		return listUsersDto;
	}

	public UserDto changePassword(Long userId, String newPassword) throws BusinessException {

		UserDto userDto = null;

		UserEntity userEntity = userService.getUserById(userId);

		if (userEntity instanceof UserEntity) {

			userEntity.setPassword(passwordEncode.encode(newPassword));
			userEntity = userService.createUser(userEntity);

			userDto = new UserDto();

			userDto.setId(userEntity.getId());
			userDto.setFirstName(userEntity.getFirstName());
			userDto.setLastName(userEntity.getLastName());
			userDto.setEmail(userEntity.getEmail());
			userDto.setUsername(userEntity.getUsername());
			userDto.setEnabled(userEntity.getEnabled());
			userDto.setCreatedAt(userEntity.getCreatedAt());
			userDto.setUpdatedAt(userEntity.getUpdatedAt());
			userDto.setPassword(null);

			if (userEntity.getRoles().size() > 0) {
				for (RoleEntity roleEntity : userEntity.getRoles()) {
					userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
				}
			}

		}

		return userDto;
	}

	public UserDto updateUser(Long userId, String firstName, String lastName) throws BusinessException {

		UserDto userDto = null;

		UserEntity userEntity = userService.getUserById(userId);

		if (!(userEntity instanceof UserEntity)) {
			throw new BusinessException("No se ha encontrado el usuario");
		}

		userEntity.setUpdatedAt(new Date());
		userEntity.setFirstName(firstName);
		userEntity.setLastName(lastName);

		userEntity = userService.createUser(userEntity);

		userDto = new UserDto();

		userDto.setId(userEntity.getId());
		userDto.setFirstName(userEntity.getFirstName());
		userDto.setLastName(userEntity.getLastName());
		userDto.setEmail(userEntity.getEmail());
		userDto.setUsername(userEntity.getUsername());
		userDto.setEnabled(userEntity.getEnabled());
		userDto.setCreatedAt(userEntity.getCreatedAt());
		userDto.setUpdatedAt(userEntity.getUpdatedAt());
		userDto.setPassword(null);

		return userDto;
	}

	public UserDto enableUser(Long userId) throws BusinessException {

		UserDto userDto = null;

		UserEntity userEntity = userService.getUserById(userId);

		if (!(userEntity instanceof UserEntity)) {
			throw new BusinessException("No se ha encontrado el usuario");
		}

		userEntity.setUpdatedAt(new Date());
		userEntity.setEnabled(true);

		userEntity = userService.createUser(userEntity);

		userDto = new UserDto();

		userDto.setId(userEntity.getId());
		userDto.setFirstName(userEntity.getFirstName());
		userDto.setLastName(userEntity.getLastName());
		userDto.setEmail(userEntity.getEmail());
		userDto.setUsername(userEntity.getUsername());
		userDto.setEnabled(userEntity.getEnabled());
		userDto.setCreatedAt(userEntity.getCreatedAt());
		userDto.setUpdatedAt(userEntity.getUpdatedAt());
		userDto.setPassword(null);
		
		if (userEntity.getRoles().size() > 0) {
			for (RoleEntity roleEntity : userEntity.getRoles()) {
				userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
			}
		}

		return userDto;
	}

	public UserDto disableUser(Long userId) throws BusinessException {

		UserDto userDto = null;

		UserEntity userEntity = userService.getUserById(userId);

		if (!(userEntity instanceof UserEntity)) {
			throw new BusinessException("No se ha encontrado el usuario");
		}

		userEntity.setUpdatedAt(new Date());
		userEntity.setEnabled(false);

		userEntity = userService.createUser(userEntity);

		userDto = new UserDto();

		userDto.setId(userEntity.getId());
		userDto.setFirstName(userEntity.getFirstName());
		userDto.setLastName(userEntity.getLastName());
		userDto.setEmail(userEntity.getEmail());
		userDto.setUsername(userEntity.getUsername());
		userDto.setEnabled(userEntity.getEnabled());
		userDto.setCreatedAt(userEntity.getCreatedAt());
		userDto.setUpdatedAt(userEntity.getUpdatedAt());
		userDto.setPassword(null);
		
		if (userEntity.getRoles().size() > 0) {
			for (RoleEntity roleEntity : userEntity.getRoles()) {
				userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
			}
		}

		return userDto;
	}

}
