package com.pfa.pfabackend.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.user.data.CodeConfirmationRepository;
import com.pfa.pfabackend.user.models.CodeConfirmation;

@Service
@RequiredArgsConstructor
public class CodeConfirmationService {
    private final CodeConfirmationRepository codeConfirmationRepository;

    public void saveCodeConfirmation(CodeConfirmation codeConfirmation){
        codeConfirmationRepository.save(codeConfirmation);
    }
}
