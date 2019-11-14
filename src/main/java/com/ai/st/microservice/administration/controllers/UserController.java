package com.ai.st.microservice.administration.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.administration.business.UserBusiness;
import com.ai.st.microservice.administration.dto.UserDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Base64;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
		RequestMethod.OPTIONS })
@Api(value = "Manage Users", description = "Manage Users", tags = { "Manage Users" })
@RestController
@RequestMapping("api/administration/users")
public class UserController {

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

}
