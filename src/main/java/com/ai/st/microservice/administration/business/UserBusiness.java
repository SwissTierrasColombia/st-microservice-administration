package com.ai.st.microservice.administration.business;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.ai.st.microservice.administration.dto.*;
import com.ai.st.microservice.administration.entities.CodeEntity;
import com.ai.st.microservice.administration.entities.RoleEntity;
import com.ai.st.microservice.administration.entities.UserEntity;
import com.ai.st.microservice.administration.exceptions.BusinessException;
import com.ai.st.microservice.administration.models.services.IRoleService;
import com.ai.st.microservice.administration.models.services.IUserService;

import com.ai.st.microservice.administration.notifier.NotifierChangeEmailService;
import com.ai.st.microservice.common.business.RoleBusiness;
import com.ai.st.microservice.common.clients.ManagerFeignClient;
import com.ai.st.microservice.common.clients.OperatorFeignClient;
import com.ai.st.microservice.common.clients.ProviderFeignClient;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerProfileDto;
import com.ai.st.microservice.common.dto.operators.MicroserviceOperatorDto;
import com.ai.st.microservice.common.dto.providers.MicroserviceProviderDto;
import com.ai.st.microservice.common.dto.providers.MicroserviceProviderProfileDto;
import com.ai.st.microservice.common.dto.providers.MicroserviceProviderRoleDto;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserBusiness {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Autowired
    private CodeBusiness codeBusiness;

    @Autowired
    private NotificationBusiness notificationBusiness;

    @Autowired
    private ManagerFeignClient managerClient;

    @Autowired
    private ProviderFeignClient providerClient;

    @Autowired
    private OperatorFeignClient operatorClient;

    @Autowired
    private NotifierChangeEmailService notifierChangeEmailService;

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
            userDto.setLastLogin(userEntity.getLastLogin());
            userDto.setAmountSuccessfulLogins(userEntity.getAmountSuccessfulLogins());

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
            userDto.setLastLogin(userEntity.getLastLogin());

            if (userEntity.getRoles().size() > 0) {
                for (RoleEntity roleEntity : userEntity.getRoles()) {
                    userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
                }
            }

        }

        return userDto;
    }

    public UserDto createUser(String firstName, String lastName, String password, String email, String username,
            boolean enabled, List<Long> roles) throws BusinessException {

        email = email.toLowerCase().trim();
        username = username.toLowerCase().trim();

        // validate roles
        List<RoleEntity> listRoles = new ArrayList<>();
        roles = roles.stream().distinct().collect(Collectors.toList());
        for (Long roleId : roles) {
            RoleEntity roleEntity = roleService.getRoleById(roleId);
            if (roleEntity == null) {
                throw new BusinessException("El rol no existe.");
            } else {
                listRoles.add(roleEntity);
            }
        }

        // validation username
        UserEntity userUsernameEntity = userService.getUserByUsername(username);
        if (userUsernameEntity != null) {
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
        userEntity.setEnabled(enabled);
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncode.encode(password));
        userEntity.setRoles(listRoles);

        userEntity = userService.createOrUpdateUser(userEntity);

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

        List<UserDto> listUsersDto = new ArrayList<>();

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
            userDto.setLastLogin(userEntity.getLastLogin());
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

        List<UserDto> listUsersDto = new ArrayList<>();

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
            userDto.setLastLogin(userEntity.getLastLogin());
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

        if (userEntity != null) {

            userEntity.setPassword(passwordEncode.encode(newPassword));
            userEntity = userService.createOrUpdateUser(userEntity);

            notifierChangeEmailService.sendNotification(userEntity.getId(), userEntity.getEmail());

            userDto = new UserDto();

            userDto.setId(userEntity.getId());
            userDto.setFirstName(userEntity.getFirstName());
            userDto.setLastName(userEntity.getLastName());
            userDto.setEmail(userEntity.getEmail());
            userDto.setUsername(userEntity.getUsername());
            userDto.setEnabled(userEntity.getEnabled());
            userDto.setCreatedAt(userEntity.getCreatedAt());
            userDto.setUpdatedAt(userEntity.getUpdatedAt());
            userDto.setLastLogin(userEntity.getLastLogin());
            userDto.setPassword(null);

            if (userEntity.getRoles().size() > 0) {
                for (RoleEntity roleEntity : userEntity.getRoles()) {
                    userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
                }
            }

        }

        return userDto;
    }

    public UserDto updateUser(Long userId, String firstName, String lastName, String email) throws BusinessException {

        UserEntity userEntity = userService.getUserById(userId);

        if (userEntity == null) {
            throw new BusinessException("No se ha encontrado el usuario");
        }

        userEntity.setUpdatedAt(new Date());
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);

        if (email != null) {
            userEntity.setEmail(email);
        }

        userEntity = userService.createOrUpdateUser(userEntity);

        UserDto userDto = new UserDto();

        userDto.setId(userEntity.getId());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        userDto.setUsername(userEntity.getUsername());
        userDto.setEnabled(userEntity.getEnabled());
        userDto.setCreatedAt(userEntity.getCreatedAt());
        userDto.setUpdatedAt(userEntity.getUpdatedAt());
        userDto.setLastLogin(userEntity.getLastLogin());
        userDto.setPassword(null);

        return userDto;
    }

    public UserDto enableUser(Long userId) throws BusinessException {

        UserDto userDto = null;

        UserEntity userEntity = userService.getUserById(userId);

        if (userEntity == null) {
            throw new BusinessException("No se ha encontrado el usuario");
        }

        userEntity.setUpdatedAt(new Date());
        userEntity.setEnabled(true);

        userEntity = userService.createOrUpdateUser(userEntity);

        userDto = new UserDto();

        userDto.setId(userEntity.getId());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        userDto.setUsername(userEntity.getUsername());
        userDto.setEnabled(userEntity.getEnabled());
        userDto.setCreatedAt(userEntity.getCreatedAt());
        userDto.setUpdatedAt(userEntity.getUpdatedAt());
        userDto.setLastLogin(userEntity.getLastLogin());
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

        if (userEntity == null) {
            throw new BusinessException("No se ha encontrado el usuario");
        }

        userEntity.setUpdatedAt(new Date());
        userEntity.setEnabled(false);

        userEntity = userService.createOrUpdateUser(userEntity);

        userDto = new UserDto();

        userDto.setId(userEntity.getId());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        userDto.setUsername(userEntity.getUsername());
        userDto.setEnabled(userEntity.getEnabled());
        userDto.setCreatedAt(userEntity.getCreatedAt());
        userDto.setUpdatedAt(userEntity.getUpdatedAt());
        userDto.setLastLogin(userEntity.getLastLogin());
        userDto.setPassword(null);

        if (userEntity.getRoles().size() > 0) {
            for (RoleEntity roleEntity : userEntity.getRoles()) {
                userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
            }
        }

        return userDto;
    }

    public void recoverAccount(String email) throws BusinessException {

        List<UserEntity> usersEntity = userService.getUsersByEmail(email);

        if (usersEntity.size() == 0) {
            throw new BusinessException("No existe un usuario registrado con el correo electrónico especificado.");
        }

        int addMinuteTime = 10;
        Date expirationDate = new Date();
        expirationDate = DateUtils.addMinutes(expirationDate, addMinuteTime);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        for (UserEntity userEntity : usersEntity) {
            boolean result = codeBusiness.unavailableCodesByUser(userEntity.getId());
            if (!result) {
                throw new BusinessException("No se ha podido generar las instrucciones de recuperación de cuenta.");
            }

            CodeEntity codeEntity = codeBusiness.createCode(userEntity, expirationDate);
            if (codeEntity == null) {
                throw new BusinessException("No se ha podido generar las instrucciones de recuperación de cuenta.");
            }

            String date = simpleDateFormat.format(expirationDate);

            // send email
            notificationBusiness.sendNotificationRecoverAccount(email, codeEntity.getCode(), userEntity.getUsername(),
                    date, userEntity.getId());
        }

    }

    public void resetAccount(String username, String code, String password) throws BusinessException {

        UserEntity userEntity = userService.getUserByUsername(username.trim());

        if (userEntity == null) {
            throw new BusinessException("No existe un usuario registrado con el username especificado.");
        }

        CodeEntity codeEntity = codeBusiness.getOTPByCodeAndUser(code, userEntity);

        if (codeEntity == null) {
            throw new BusinessException("El código OTP es inválido.");
        }

        if (!codeEntity.getAvailable()) {
            throw new BusinessException("El código OTP es inválido.");
        }

        if (password.length() <= 5) {
            throw new BusinessException("La nueva contraseña debe tener mínimo 6 caracteres.");
        }

        // validate expiration date
        Date now = new Date();
        if (now.after(codeEntity.getExpiredAt())) {
            throw new BusinessException("El código OTP ha expirado!.");
        }

        changePassword(userEntity.getId(), password);

        codeBusiness.unavailableCodesByUser(userEntity.getId());
    }

    public List<ManagerUserDto> getManagerUsers(Long managerCode) {

        List<UserEntity> managerUsers = userService
                .getUserByRoles(new ArrayList<>(Collections.singletonList(RoleBusiness.ROLE_MANAGER)));

        List<ManagerUserDto> users = new ArrayList<>();

        for (UserEntity userEntity : managerUsers) {

            MicroserviceManagerDto managerDto = managerClient.findByUserCode(userEntity.getId());

            if (managerDto != null) {
                List<MicroserviceManagerProfileDto> profiles = managerClient.findProfilesByUser(userEntity.getId());
                ManagerUserDto managerUserDto = new ManagerUserDto();

                managerUserDto.setId(userEntity.getId());
                managerUserDto.setFirstName(userEntity.getFirstName());
                managerUserDto.setLastName(userEntity.getLastName());
                managerUserDto.setEmail(userEntity.getEmail());
                managerUserDto.setUsername(userEntity.getUsername());
                managerUserDto.setEnabled(userEntity.getEnabled());
                managerUserDto.setCreatedAt(userEntity.getCreatedAt());
                managerUserDto.setUpdatedAt(userEntity.getUpdatedAt());
                managerUserDto.setPassword(null);
                managerUserDto.setProfiles(profiles);
                managerUserDto.setManager(managerDto);

                users.add(managerUserDto);
            }

        }

        if (managerCode != null) {
            users = users.stream().filter(u -> u.getManager().getId().equals(managerCode)).collect(Collectors.toList());
        }

        return users;
    }

    public List<ProviderUserDto> getProviderUsers(Long providerCode) {

        List<UserEntity> providerUsers = userService
                .getUserByRoles(new ArrayList<>(Collections.singletonList(RoleBusiness.ROLE_SUPPLY_SUPPLIER)));

        List<ProviderUserDto> users = new ArrayList<>();

        for (UserEntity userEntity : providerUsers) {

            ProviderUserDto providerUserDto = new ProviderUserDto();

            providerUserDto.setId(userEntity.getId());
            providerUserDto.setFirstName(userEntity.getFirstName());
            providerUserDto.setLastName(userEntity.getLastName());
            providerUserDto.setEmail(userEntity.getEmail());
            providerUserDto.setUsername(userEntity.getUsername());
            providerUserDto.setEnabled(userEntity.getEnabled());
            providerUserDto.setCreatedAt(userEntity.getCreatedAt());
            providerUserDto.setUpdatedAt(userEntity.getUpdatedAt());
            providerUserDto.setPassword(null);

            boolean found = false;
            try {
                List<MicroserviceProviderRoleDto> profiles = providerClient.findRolesByUser(userEntity.getId());
                providerUserDto.setRoles(profiles);

                MicroserviceProviderDto providerDto = providerClient.findProviderByAdministrator(userEntity.getId());
                providerUserDto.setProvider(providerDto);
                found = true;
            } catch (Exception ignored) {

            }

            try {
                List<MicroserviceProviderProfileDto> profiles = providerClient.findProfilesByUser(userEntity.getId());
                providerUserDto.setProfiles(profiles);

                MicroserviceProviderDto providerDto = providerClient.findByUserCode(userEntity.getId());
                providerUserDto.setProvider(providerDto);

                found = true;
            } catch (Exception ignored) {

            }

            if (found) {
                users.add(providerUserDto);
            }

        }

        if (providerCode != null) {
            users = users.stream().filter(u -> u.getProvider().getId().equals(providerCode))
                    .collect(Collectors.toList());
        }

        return users;
    }

    public List<OperatorUserDto> getOperatorUsers(Long operatorCode) {

        List<UserEntity> managerUsers = userService
                .getUserByRoles(new ArrayList<>(Collections.singletonList(RoleBusiness.ROLE_OPERATOR)));

        List<OperatorUserDto> users = new ArrayList<>();

        for (UserEntity userEntity : managerUsers) {

            MicroserviceOperatorDto operatorDto = operatorClient.findByUserCode(userEntity.getId());

            if (operatorDto != null) {

                OperatorUserDto operatorUserDto = new OperatorUserDto();

                operatorUserDto.setId(userEntity.getId());
                operatorUserDto.setFirstName(userEntity.getFirstName());
                operatorUserDto.setLastName(userEntity.getLastName());
                operatorUserDto.setEmail(userEntity.getEmail());
                operatorUserDto.setUsername(userEntity.getUsername());
                operatorUserDto.setEnabled(userEntity.getEnabled());
                operatorUserDto.setCreatedAt(userEntity.getCreatedAt());
                operatorUserDto.setUpdatedAt(userEntity.getUpdatedAt());
                operatorUserDto.setPassword(null);
                operatorUserDto.setOperator(operatorDto);

                users.add(operatorUserDto);
            }

        }

        if (operatorCode != null) {
            users = users.stream().filter(u -> u.getOperator().getId().equals(operatorCode))
                    .collect(Collectors.toList());
        }

        return users;
    }

    public UserDto updateLogin(String username) {

        UserDto userDto = null;

        UserEntity userEntity = userService.getUserByUsername(username);

        if (userEntity != null) {

            int amountSuccessfulLogins = 1;
            if (userEntity.getAmountSuccessfulLogins() != null) {
                amountSuccessfulLogins = userEntity.getAmountSuccessfulLogins() + 1;
            }

            userEntity.setLastLogin(new Date());
            userEntity.setAmountSuccessfulLogins(amountSuccessfulLogins);
            userEntity = userService.createOrUpdateUser(userEntity);

            userDto = new UserDto();

            userDto.setId(userEntity.getId());
            userDto.setFirstName(userEntity.getFirstName());
            userDto.setLastName(userEntity.getLastName());
            userDto.setEmail(userEntity.getEmail());
            userDto.setUsername(userEntity.getUsername());
            userDto.setEnabled(userEntity.getEnabled());
            userDto.setCreatedAt(userEntity.getCreatedAt());
            userDto.setUpdatedAt(userEntity.getUpdatedAt());
            userDto.setLastLogin(userEntity.getLastLogin());
            userDto.setAmountSuccessfulLogins(userEntity.getAmountSuccessfulLogins());
            userDto.setPassword(null);

            if (userEntity.getRoles().size() > 0) {
                for (RoleEntity roleEntity : userEntity.getRoles()) {
                    userDto.getRoles().add(new RoleDto(roleEntity.getId(), roleEntity.getName()));
                }
            }

        }

        return userDto;
    }

}
