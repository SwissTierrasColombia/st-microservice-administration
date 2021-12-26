package com.ai.st.microservice.administration.controllers.v1;

import java.util.List;
import java.util.Map;

import com.ai.st.microservice.administration.dto.*;
import com.ai.st.microservice.common.business.AdministrationBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ai.st.microservice.administration.business.UserBusiness;
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

    @Autowired
    private AdministrationBusiness administrationBusiness;

    @GetMapping("/login")
    @ApiOperation(value = "Search user by username for login")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User found", response = UserDto.class),
            @ApiResponse(code = 404, message = "User Not Found"), @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<UserDto> searchUser(@RequestParam(required = true, name = "username") String username) {

        HttpStatus httpStatus;
        UserDto userDto = null;

        try {
            userDto = userBusiness.getUserByUsername(username);
            httpStatus = (userDto != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        } catch (Exception e) {
            log.error("Error UserController@searchUser ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(userDto, httpStatus);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User found", response = UserDto.class),
            @ApiResponse(code = 404, message = "User Not Found"), @ApiResponse(code = 500, message = "Error Server")})
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User found", response = UserDto.class),
            @ApiResponse(code = 404, message = "User Not Found"), @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<UserDto> searchUserByToken(@RequestParam(required = true, name = "token") String jwtToken) {

        HttpStatus httpStatus;
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
    @ApiResponses(value = {@ApiResponse(code = 201, message = "User created", response = UserDto.class),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> createUser(@RequestBody CreateUserDto requestCreateUser) {

        HttpStatus httpStatus;
        Object responseDto;

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

            responseDto = userBusiness.createUser(firstName, lastName, password, email, username, requestCreateUser.isEnabled(), roles);
            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error UserController@createUser#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (BusinessException e) {
            log.error("Error UserController@createUser#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@createUser#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping("")
    @ApiOperation(value = "Get users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get users", response = UserDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> getUsers(@RequestParam(name = "roles", required = false) List<Long> roles) {

        Object responseDto;
        HttpStatus httpStatus;

        try {

            if (roles != null && roles.size() > 0) {
                responseDto = userBusiness.getUsersByRoles(roles);
            } else {
                responseDto = userBusiness.getUsers();
            }

            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error UserController@getUsers#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@getUsers#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PostMapping("/{id}/reset-password")
    @ApiOperation(value = "Reset password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reset password", response = UserDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> changePassword(@PathVariable(required = true, name = "id") Long userId,
                                                 @RequestBody ChangePasswordDto requestChangePassword) {

        Object responseDto;
        HttpStatus httpStatus;

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
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (BusinessException e) {
            log.error("Error UserController@changePassword#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@changePassword#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PutMapping("/{userId}")
    @ApiOperation(value = "Update user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User updated", response = UserDto.class),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UpdateUserDto requestUpdateUser) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            // validation first name
            String firstName = requestUpdateUser.getFirstName();
            if (firstName == null || firstName.isEmpty()) {
                throw new InputValidationException("El nombre es requerido");
            }

            // validation last name
            String lastName = requestUpdateUser.getLastName();
            if (lastName == null || lastName.isEmpty()) {
                throw new InputValidationException("El apellido es requerido");
            }

            responseDto = userBusiness.updateUser(userId, firstName, lastName, requestUpdateUser.getEmail());
            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error UserController@updateUser#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (BusinessException e) {
            log.error("Error UserController@updateUser#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@updateUser#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PutMapping("/{userId}/enable")
    @ApiOperation(value = "Update user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User enabled", response = UserDto.class),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> enableUser(@PathVariable Long userId) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            responseDto = userBusiness.enableUser(userId);
            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error UserController@enableUser#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@enableUser#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PutMapping("/{userId}/disable")
    @ApiOperation(value = "Update user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User disabled", response = UserDto.class),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> disableUser(@PathVariable Long userId) {

        HttpStatus httpStatus = null;
        Object responseDto = null;

        try {

            responseDto = userBusiness.disableUser(userId);
            httpStatus = HttpStatus.OK;

        } catch (BusinessException e) {
            log.error("Error UserController@disableUser#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@disableUser#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PutMapping("/recover")
    @ApiOperation(value = "Recover account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OTP generated"),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> recoverAccount(@RequestBody RecoverAccountDto recoverAccount) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            // validation email
            String email = recoverAccount.getEmail();
            if (email == null || email.isEmpty()) {
                throw new InputValidationException("El correo electrónico es requerido.");
            }

            userBusiness.recoverAccount(email);
            responseDto = new BasicResponseDto(
                    "Se ha enviado un correo electrónico con las instrucciones para recuperar la cuenta", 7);
            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error UserController@recoverAccount#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (BusinessException e) {
            log.error("Error UserController@recoverAccount#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@recoverAccount#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PutMapping("/reset")
    @ApiOperation(value = "Reset account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Password changed"),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<Object> resetAccount(@RequestBody ResetAccountDto recoverAccount) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            // validation username
            String username = recoverAccount.getUsername();
            if (username == null || username.isEmpty()) {
                throw new InputValidationException("El correo electrónico es requerido.");
            }

            // validation password
            String password = recoverAccount.getNewPassword();
            if (password == null || password.isEmpty()) {
                throw new InputValidationException("La nueva contraseña es requerido.");
            }

            // validation password
            String code = recoverAccount.getCode();
            if (code == null || code.isEmpty()) {
                throw new InputValidationException("El código OTP es requerido.");
            }

            userBusiness.resetAccount(username, code, password);
            responseDto = new BasicResponseDto("Se ha actualizado la contraseña correctamente!", 7);
            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error UserController@resetAccount#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (BusinessException e) {
            log.error("Error UserController@resetAccount#Business ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.getMessage(), 2);
        } catch (Exception e) {
            log.error("Error UserController@resetAccount#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping("/managers")
    @ApiOperation(value = "Get managers users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get users", response = ManagerUserDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<?> getManagerUsers(@RequestParam(name = "manager", required = false) Long managerCode) {

        Object responseDto;
        HttpStatus httpStatus;

        try {

            responseDto = userBusiness.getManagerUsers(managerCode);
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {
            log.error("Error UserController@getManagerUsers#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping("/providers")
    @ApiOperation(value = "Get providers users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get users", response = ProviderUserDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<?> getProviderUsers(@RequestParam(name = "provider", required = false) Long providerCode) {

        Object responseDto;
        HttpStatus httpStatus;

        try {

            responseDto = userBusiness.getProviderUsers(providerCode);
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {
            log.error("Error UserController@getProviderUsers#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping("/operators")
    @ApiOperation(value = "Get operators users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get users", response = OperatorUserDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server")})
    public ResponseEntity<?> getOperatorUsers(@RequestParam(name = "operator", required = false) Long operatorCode) {

        Object responseDto;
        HttpStatus httpStatus;

        try {

            responseDto = userBusiness.getOperatorUsers(operatorCode);
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {
            log.error("Error UserController@getOperatorUsers#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
