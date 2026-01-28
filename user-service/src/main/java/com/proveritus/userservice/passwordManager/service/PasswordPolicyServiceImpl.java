package com.proveritus.userservice.passwordManager.service;

import com.tinash.cloud.utility.password.dto.PasswordPolicyDTO;
import com.proveritus.userservice.passwordManager.mapper.PasswordPolicyMapper;
import com.proveritus.userservice.domain.repository.PasswordPolicyRepository;
import com.proveritus.userservice.domain.model.user.PasswordPolicy;
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
    public PasswordPolicyDTO getPasswordPolicy() {
        return passwordPolicyRepository.findFirstByOrderByIdAsc()
                .map(passwordPolicyMapper::toDto)
                .orElseGet(() -> {
                    PasswordPolicy newPolicy = passwordPolicyRepository.save(new PasswordPolicy());
                    return passwordPolicyMapper.toDto(newPolicy);
                });
    }

    @Override
    @Transactional
    public PasswordPolicyDTO updatePasswordPolicy(PasswordPolicyDTO passwordPolicyDto) {
        PasswordPolicy policy = passwordPolicyRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> passwordPolicyRepository.save(new PasswordPolicy()));

        passwordPolicyMapper.updateEntityFromDto(passwordPolicyDto, policy);
        PasswordPolicy updatedPolicy = passwordPolicyRepository.save(policy);
        return passwordPolicyMapper.toDto(updatedPolicy);
    }
}
