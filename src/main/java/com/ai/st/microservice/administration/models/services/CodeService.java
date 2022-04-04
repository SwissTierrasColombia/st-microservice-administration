package com.ai.st.microservice.administration.models.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.administration.entities.CodeEntity;
import com.ai.st.microservice.administration.entities.UserEntity;
import com.ai.st.microservice.administration.models.repositories.CodeRepository;

@Service
public class CodeService implements ICodeService {

    @Autowired
    private CodeRepository codeRepository;

    @Override
    @Transactional
    public void unavailableCodesByUser(Long userId) {
        codeRepository.unavailableCodesByUser(userId);
    }

    @Override
    @Transactional
    public CodeEntity createCode(CodeEntity codeEntity) {
        return codeRepository.save(codeEntity);
    }

    @Override
    public CodeEntity getOPTbyCodeAndUser(String code, UserEntity user) {
        return codeRepository.findByCodeAndUser(code, user);
    }

}
