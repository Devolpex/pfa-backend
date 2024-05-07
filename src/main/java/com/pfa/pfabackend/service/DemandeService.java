package com.pfa.pfabackend.service;

import com.pfa.pfabackend.enums.DemandeStatus;
import com.pfa.pfabackend.enums.DemandeType;
import com.pfa.pfabackend.model.Client;
import com.pfa.pfabackend.model.Demande;
import com.pfa.pfabackend.repository.DemandeRepository;
import com.pfa.pfabackend.request.demande.DemandeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DemandeService {
    private final DemandeRepository demandeRepository;
    private final ClientService clientService;

    public void saveDemande(DemandeCreateRequest request)  {
        Demande demande = convertRequestToEntity(request);
        demandeRepository.save(demande);
    }

    private Demande convertRequestToEntity(DemandeCreateRequest request)  {
        Demande demande = new Demande();
        demande.setDescription(request.getDescription());
        demande.setType(DemandeType.valueOf(request.getType_demande()));
        demande.setDate(new java.sql.Date(new Date().getTime()));
        demande.setStatus(DemandeStatus.PENDING);
        Client client = clientService.findClientById(request.getClient_id());
        demande.setClient(client);
        return demande;
    }
}
