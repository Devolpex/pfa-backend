package com.pfa.pfabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.model.CodeConfirmation;
import com.pfa.pfabackend.repository.CodeConfirmationRepository;

@Service
@RequiredArgsConstructor
public class CodeConfirmationService {
    private final CodeConfirmationRepository codeConfirmationRepository;

    public void saveCodeConfirmation(CodeConfirmation codeConfirmation){
        codeConfirmationRepository.save(codeConfirmation);
    }
}
