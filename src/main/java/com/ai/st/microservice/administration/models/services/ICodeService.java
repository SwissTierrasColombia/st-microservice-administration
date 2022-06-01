package com.ai.st.microservice.administration.models.services;

import com.ai.st.microservice.administration.entities.CodeEntity;
import com.ai.st.microservice.administration.entities.UserEntity;

public interface ICodeService {

    public void unavailableCodesByUser(Long userId);

    public CodeEntity createCode(CodeEntity codeEntity);

    public CodeEntity getOPTbyCodeAndUser(String code, UserEntity user);

}
