package com.tinash.userservice.passwordManager.service;

import com.tinash.userservice.passwordManager.dto.PasswordPolicyDto;
import com.tinash.userservice.passwordManager.mapper.PasswordPolicyMapper;
import com.tinash.userservice.domain.repository.PasswordPolicyRepository;
import com.tinash.userservice.domain.model.password.PasswordPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordPolicyServiceImpl implements PasswordPolicyService {

    private final PasswordPolicyRepository passwordPolicyRepository;
    private final PasswordPolicyMapper passwordPolicyMapper;

    @Override
    @Transactional(readOnly = true)
    public PasswordPolicyDto getPasswordPolicy() {
        return passwordPolicyRepository.findFirstByOrderByIdAsc()
                .map(passwordPolicyMapper::toDto)
                .orElseGet(() -> {
                    PasswordPolicy newPolicy = passwordPolicyRepository.save(new PasswordPolicy());
                    return passwordPolicyMapper.toDto(newPolicy);
                });
    }

    @Override
    @Transactional
    public PasswordPolicyDto updatePasswordPolicy(PasswordPolicyDto passwordPolicyDto) {
        PasswordPolicy policy = passwordPolicyRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> passwordPolicyRepository.save(new PasswordPolicy()));

        passwordPolicyMapper.updateEntityFromDto(passwordPolicyDto, policy);
        PasswordPolicy updatedPolicy = passwordPolicyRepository.save(policy);
        return passwordPolicyMapper.toDto(updatedPolicy);
    }
}
