package com.ai.st.microservice.administration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ai.st.microservice.common.business.RoleBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.administration.entities.RoleEntity;
import com.ai.st.microservice.administration.entities.UserEntity;
import com.ai.st.microservice.administration.models.services.IRoleService;
import com.ai.st.microservice.administration.models.services.IUserService;

@Component
public class StMicroserviceAdministrationApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${st.root.username}")
    private String rootUsername;

    @Value("${st.root.password}")
    private String rootPassword;

    private static final Logger log = LoggerFactory.getLogger(StMicroserviceAdministrationApplicationStartup.class);

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("ST - Loading Domains ... ");
        this.initRoles();
        this.initUsers();
    }

    public void initRoles() {
        Long countRoles = roleService.getAllCount();
        if (countRoles == 0) {

            try {
                RoleEntity roleAdministrator = new RoleEntity();
                roleAdministrator.setId(RoleBusiness.ROLE_ADMINISTRATOR);
                roleAdministrator.setName("ADMINISTRADOR");
                roleService.createRole(roleAdministrator);

                RoleEntity roleManager = new RoleEntity();
                roleManager.setId(RoleBusiness.ROLE_MANAGER);
                roleManager.setName("GESTOR");
                roleService.createRole(roleManager);

                RoleEntity roleOperator = new RoleEntity();
                roleOperator.setId(RoleBusiness.ROLE_OPERATOR);
                roleOperator.setName("OPERADOR");
                roleService.createRole(roleOperator);

                RoleEntity roleProvider = new RoleEntity();
                roleProvider.setId(RoleBusiness.ROLE_SUPPLY_SUPPLIER);
                roleProvider.setName("PROVEEDOR INSUMO");
                roleService.createRole(roleProvider);

                RoleEntity roleSuper = new RoleEntity();
                roleSuper.setId(RoleBusiness.ROLE_SUPER_ADMINISTRATOR);
                roleSuper.setName("SUPER ADMINISTRADOR");
                roleService.createRole(roleSuper);

                log.info("The domains 'roles' have been loaded!");
            } catch (Exception e) {
                log.error("Failed to load 'roles' domains");
            }

        }
    }

    public void initUsers() {
        Long countUsers = userService.getAllCount();
        if (countUsers == 0) {

            try {

                RoleEntity roleSuper = roleService.getRoleById(RoleBusiness.ROLE_SUPER_ADMINISTRATOR);

                // User 1

                UserEntity userToTest1 = new UserEntity();
                userToTest1.setFirstName("Super");
                userToTest1.setLastName("Administración");
                userToTest1.setCreatedAt(new Date());
                userToTest1.setEmail("root@st.com");
                userToTest1.setEnabled(true);
                userToTest1.setUsername(rootUsername);
                userToTest1.setPassword(passwordEncode.encode(rootPassword));

                List<RoleEntity> listRoles1 = new ArrayList<RoleEntity>();
                listRoles1.add(roleSuper);
                userToTest1.setRoles(listRoles1);

                userService.createOrUpdateUser(userToTest1);

                log.info("The domains 'users' have been loaded!");
            } catch (Exception e) {
                log.error("Failed to load 'users' domains: " + e.getMessage());
            }

        }
    }

}
