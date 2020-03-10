package com.ai.st.microservice.administration.controllers.v1;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.administration.business.UserBusiness;
import com.ai.st.microservice.administration.dto.ChangePasswordDto;
import com.ai.st.microservice.administration.dto.CreateUserDto;
import com.ai.st.microservice.administration.dto.ErrorDto;
import com.ai.st.microservice.administration.dto.UserDto;
import com.ai.st.microservice.administration.exceptions.BusinessException;
import com.ai.st.microservice.administration.exceptions.InputValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Base64;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/administration/v1/users")
public class UserV1Controller {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserBusiness userBusiness;

	@GetMapping("/login")
	@ApiOperation(value = "Search user by username for login")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User found", response = UserDto.class),
			@ApiResponse(code = 404, message = "User Not Found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<UserDto> searchUser(@RequestParam(required = true, name = "username") String username) {

		HttpStatus httpStatus = null;
		UserDto userDto = null;

		try {
			userDto = userBusiness.getUserByUsername(username);
			httpStatus = (userDto instanceof UserDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (Exception e) {
			log.error("Error UserController@searchUser ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(userDto, httpStatus);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Get user by id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User found", response = UserDto.class),
			@ApiResponse(code = 404, message = "User Not Found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<UserDto> getUserById(@PathVariable(required = true, name = "id") Long userId) {

		HttpStatus httpStatus = null;
		UserDto userDto = null;

		try {
			userDto = userBusiness.getUserById(userId);
			userDto.setPassword("");
			httpStatus = (userDto instanceof UserDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (Exception e) {
			log.error("Error UserController@getUserById ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(userDto, httpStatus);
	}

	@GetMapping("/token")
	@ApiOperation(value = "Search user by token")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User found", response = UserDto.class),
			@ApiResponse(code = 404, message = "User Not Found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<UserDto> searchUserByToken(@RequestParam(required = true, name = "token") String jwtToken) {

		HttpStatus httpStatus = null;
		UserDto userDto = null;

		try {

			String[] split_string = jwtToken.split("\\.");
			String base64EncodedBody = split_string[1];

			Base64 base64Url = new Base64(true);
			String body = new String(base64Url.decode(base64EncodedBody));

			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = mapper.readValue(body, Map.class);
			String username = map.get("user_name");

			userDto = userBusiness.getUserByUsername(username);
			httpStatus = (userDto instanceof UserDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (Exception e) {
			log.error("Error UserController@searchUserByToken ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(userDto, httpStatus);
	}

	@PostMapping("")
	@ApiOperation(value = "Create user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "User created", response = UserDto.class),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<Object> createUser(@RequestBody CreateUserDto requestCreateUser) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

			// validation first name
			String firstName = requestCreateUser.getFirstName();
			if (firstName == null || firstName.isEmpty()) {
				throw new InputValidationException("El nombre es requerido");
			}

			// validation last name
			String lastName = requestCreateUser.getLastName();
			if (lastName == null || lastName.isEmpty()) {
				throw new InputValidationException("El apellido es requerido");
			}

			// validation password
			String password = requestCreateUser.getPassword();

			// validation username
			String username = requestCreateUser.getUsername();
			if (username == null || username.isEmpty()) {
				throw new InputValidationException("El nombre de usuario es requerido.");
			}

			// validation email
			String email = requestCreateUser.getEmail();
			if (email == null || email.isEmpty()) {
				throw new InputValidationException("El correo electrónico es requerido.");
			}

			List<Long> roles = requestCreateUser.getRoles();
			if (roles.size() == 0) {
				throw new InputValidationException("Se debe especificar al menos un rol para el usuario.");
			}

			responseDto = userBusiness.createUser(firstName, lastName, password, email, username, roles);
			httpStatus = HttpStatus.OK;

		} catch (InputValidationException e) {
			log.error("Error UserController@createUser#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error UserController@createUser#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error UserController@createUser#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@GetMapping("")
	@ApiOperation(value = "Get users")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get users", response = UserDto.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<Object> getUsers() {

		Object responseDto = null;
		HttpStatus httpStatus = null;

		try {

			responseDto = userBusiness.getUsers();
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error UserController@getUsers#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error UserController@getUsers#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@PostMapping("/{id}/reset-password")
	@ApiOperation(value = "Reset password")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reset password", response = UserDto.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<Object> changePassword(@PathVariable(required = true, name = "id") Long userId,
			@RequestBody ChangePasswordDto requestChangePassword) {

		Object responseDto = null;
		HttpStatus httpStatus = null;

		try {

			// validation password
			String password = requestChangePassword.getPassword();
			if (password == null || password.isEmpty()) {
				throw new InputValidationException("La contraseña es requerida.");
			}

			responseDto = userBusiness.changePassword(userId, password);
			httpStatus = HttpStatus.OK;

		} catch (InputValidationException e) {
			log.error("Error UserController@changePassword#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error UserController@changePassword#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error UserController@changePassword#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

}
