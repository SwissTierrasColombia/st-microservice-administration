package com.ai.st.microservice.administration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.administration.business.RoleBusiness;
import com.ai.st.microservice.administration.entities.RoleEntity;
import com.ai.st.microservice.administration.entities.UserEntity;
import com.ai.st.microservice.administration.models.services.IRoleService;
import com.ai.st.microservice.administration.models.services.IUserService;

@Component
public class StMicroserviceAdministrationApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	private static final Logger log = LoggerFactory.getLogger(StMicroserviceAdministrationApplicationStartup.class);

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IUserService userService;

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

				RoleEntity roleAdministrator = roleService.getRoleById(RoleBusiness.ROLE_ADMINISTRATOR);
				RoleEntity roleManager = roleService.getRoleById(RoleBusiness.ROLE_MANAGER);
				RoleEntity roleProvider = roleService.getRoleById(RoleBusiness.ROLE_SUPPLY_SUPPLIER);
				RoleEntity roleOperator = roleService.getRoleById(RoleBusiness.ROLE_OPERATOR);

				// User 1

				UserEntity userToTest1 = new UserEntity();
				userToTest1.setFirstName("Agencia");
				userToTest1.setLastName("Implementación");
				userToTest1.setCreatedAt(new Date());
				userToTest1.setEmail("agenciadeimplementacion@incige.com");
				userToTest1.setEnabled(true);
				userToTest1.setUsername("agencia");
				userToTest1.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

				List<RoleEntity> listRoles1 = new ArrayList<RoleEntity>();
				listRoles1.add(roleAdministrator);
				userToTest1.setRoles(listRoles1);

				userService.createUser(userToTest1);

				if (activeProfile.equals("develop")) {
					// User 2

					UserEntity userToTest2 = new UserEntity();
					userToTest2.setFirstName("German");
					userToTest2.setLastName("Carrillo");
					userToTest2.setCreatedAt(new Date());
					userToTest2.setEmail("carrillo.german@gmail.com");
					userToTest2.setEnabled(true);
					userToTest2.setUsername("gcarrillo");
					userToTest2.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

					List<RoleEntity> listRoles2 = new ArrayList<RoleEntity>();
					listRoles2.add(roleManager);
					userToTest2.setRoles(listRoles2);

					userService.createUser(userToTest2);

					// User 3

					UserEntity userToTest3 = new UserEntity();
					userToTest3.setFirstName("Felipe");
					userToTest3.setLastName("Cano");
					userToTest3.setCreatedAt(new Date());
					userToTest3.setEmail("felipecanol@gmail.com");
					userToTest3.setEnabled(true);
					userToTest3.setUsername("fcano");
					userToTest3.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

					List<RoleEntity> listRoles3 = new ArrayList<RoleEntity>();
					listRoles3.add(roleManager);
					userToTest3.setRoles(listRoles3);

					userService.createUser(userToTest3);

					// User 4

					UserEntity userToTest4 = new UserEntity();
					userToTest4.setFirstName("Andres");
					userToTest4.setLastName("Acosta");
					userToTest4.setCreatedAt(new Date());
					userToTest4.setEmail("amacostapulido@gmail.com");
					userToTest4.setEnabled(true);
					userToTest4.setUsername("aacosta");
					userToTest4.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

					List<RoleEntity> listRoles4 = new ArrayList<RoleEntity>();
					listRoles4.add(roleProvider);
					userToTest4.setRoles(listRoles4);

					userService.createUser(userToTest4);

					// User 5

					UserEntity userToTest5 = new UserEntity();
					userToTest5.setFirstName("Jonathan");
					userToTest5.setLastName("Albarracin");
					userToTest5.setCreatedAt(new Date());
					userToTest5.setEmail("jonyfido@gmail.com");
					userToTest5.setEnabled(true);
					userToTest5.setUsername("jalbarracin");
					userToTest5.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

					List<RoleEntity> listRoles5 = new ArrayList<RoleEntity>();
					listRoles5.add(roleProvider);
					userToTest5.setRoles(listRoles5);

					userService.createUser(userToTest5);

					// User 6
					UserEntity userToTest6 = new UserEntity();
					userToTest6.setFirstName("Jhon Freddy");
					userToTest6.setLastName("Rondon Betancourt");
					userToTest6.setCreatedAt(new Date());
					userToTest6.setEmail("jhonfr29@gmail.com");
					userToTest6.setEnabled(true);
					userToTest6.setUsername("jrondon");
					userToTest6.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

					List<RoleEntity> listRoles6 = new ArrayList<RoleEntity>();
					listRoles6.add(roleAdministrator);
					userToTest6.setRoles(listRoles6);

					userService.createUser(userToTest6);

					// User 7

					UserEntity userToTest7 = new UserEntity();
					userToTest7.setFirstName("Jhon Alexander");
					userToTest7.setLastName("Galindo");
					userToTest7.setCreatedAt(new Date());
					userToTest7.setEmail("jhonalex.ue@gmail.com");
					userToTest7.setEnabled(true);
					userToTest7.setUsername("jgalindo");
					userToTest7.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

					List<RoleEntity> listRoles7 = new ArrayList<RoleEntity>();
					listRoles7.add(roleProvider);
					userToTest7.setRoles(listRoles7);

					userService.createUser(userToTest7);

					// User 8

					UserEntity userToTest8 = new UserEntity();
					userToTest8.setId((long)10);
					userToTest8.setFirstName("Leonardo");
					userToTest8.setLastName("Cardona");
					userToTest8.setCreatedAt(new Date());
					userToTest8.setEmail("leocardonapiedrahita@gmail.com");
					userToTest8.setEnabled(true);
					userToTest8.setUsername("lcardona");
					userToTest8.setPassword("$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC");

					List<RoleEntity> listRoles8 = new ArrayList<RoleEntity>();
					listRoles8.add(roleOperator);
					userToTest8.setRoles(listRoles8);

					userService.createUser(userToTest8);
				}

				log.info("The domains 'users' have been loaded!");
			} catch (Exception e) {
				log.error("Failed to load 'users' domains: " + e.getMessage());
			}

		}
	}

}
