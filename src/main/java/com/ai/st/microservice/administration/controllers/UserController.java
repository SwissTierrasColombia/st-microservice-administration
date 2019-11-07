package com.ai.st.microservice.administration.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.microservice.administration.business.UserBusiness;
import com.ai.st.microservice.administration.controllers.transfers.UserTransfer;

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

	@GetMapping("/search")
	@ApiOperation(value = "Search user by username")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User found", response = UserTransfer.class),
			@ApiResponse(code = 404, message = "User Not Found"),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<UserTransfer> searchUser(@RequestParam(required = true, name = "username") String username) {

		HttpStatus httpStatus = null;
		UserTransfer userTransfer = null;

		try {

			UserEntity userEntity = userBusiness.getUserByUsername(username);

			if (userEntity instanceof UserEntity) {
				userTransfer = new UserTransfer();
				userTransfer.setId(userEntity.getId());
				userTransfer.setFirstName(userEntity.getFirstName());
				userTransfer.setLastName(userEntity.getLastName());
				userTransfer.setEmail(userEntity.getEmail());
				userTransfer.setUsername(userEntity.getUsername());
				userTransfer.setEnabled(userEntity.getEnabled());
				userTransfer.setCreatedAt(userEntity.getCreatedAt());
				userTransfer.setUpdatedAt(userEntity.getUpdatedAt());

				httpStatus = HttpStatus.OK;
			} else {

				httpStatus = HttpStatus.NOT_FOUND;
			}

		} catch (Exception e) {
			log.error("Error UserController ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(userTransfer, httpStatus);
	}

}
