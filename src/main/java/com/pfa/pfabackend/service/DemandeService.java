package com.pfa.pfabackend.service;

import com.pfa.pfabackend.model.Admin;
import com.pfa.pfabackend.model.Demande;
import com.pfa.pfabackend.model.User;
import com.pfa.pfabackend.repository.DemandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemandeService {
    private final DemandeRepository demandeRepository;

    public Demande findDemandeById(long id) {
        Demande demande = demandeRepository.findById(id).orElse( null);
        return demande;
    }
}
