package com.ai.st.microservice.administration.business;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.administration.entities.CodeEntity;
import com.ai.st.microservice.administration.entities.UserEntity;
import com.ai.st.microservice.administration.models.services.ICodeService;

@Component
public class CodeBusiness {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ICodeService codeService;

	public boolean unavailableCodesByUser(Long userId) {

		Boolean result = false;

		try {
			codeService.unavailableCodesByUser(userId);
			result = true;
		} catch (Exception e) {
			log.error("Error deshabilitando los códigos por usuario: " + e.getMessage());
		}

		return result;
	}

	public CodeEntity createCode(UserEntity user, Date expirationDate) {

		CodeEntity codeEntity = null;

		try {

			String code = RandomStringUtils.random(6, false, true);

			codeEntity = new CodeEntity();
			codeEntity.setAvailable(true);
			codeEntity.setCode(code);
			codeEntity.setCreatedAt(new Date());
			codeEntity.setExpiredAt(expirationDate);
			codeEntity.setUser(user);

			codeEntity = codeService.createCode(codeEntity);

		} catch (Exception e) {
			codeEntity = null;
			log.error("Error creando código OTP: " + e.getMessage());
		}

		return codeEntity;
	}

	public CodeEntity getOTPByCodeAndUser(String code, UserEntity user) {

		CodeEntity codeEntity = null;

		try {

			codeEntity = codeService.getOPTbyCodeAndUser(code, user);

		} catch (Exception e) {
			log.error("No se ha podido buscar  el código OPT: " + e.getMessage());
		}

		return codeEntity;
	}

}
