package com.pfa.pfabackend.service;

import com.pfa.pfabackend.dto.client.ClientDto;
import com.pfa.pfabackend.dto.demande.DemandeDto;
import com.pfa.pfabackend.dto.user.UserDTO;
import com.pfa.pfabackend.enums.DemandeStatus;
import com.pfa.pfabackend.enums.DemandeType;
import com.pfa.pfabackend.model.Client;
import com.pfa.pfabackend.model.Demande;
import com.pfa.pfabackend.repository.DemandeRepository;
import com.pfa.pfabackend.request.demande.DemandeCreateRequest;
import com.pfa.pfabackend.request.demande.DemandeUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

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
        demande.setType(DemandeType.valueOf(request.getTypeDemande()));
        demande.setDate(new java.sql.Date(new Date().getTime()));
        demande.setStatus(DemandeStatus.PENDING);
        Client client = clientService.findClientById(request.getClientId());
        demande.setClient(client);
        return demande;
    }
//    public Demande findDemandeById(long id) {
//        Demande demande = demandeRepository.findById(id).orElse( null);
//        return demande;
//    }


    private DemandeDto convertToDTO(Demande demande){
         long clientID = demande.getClient().getId();
         String clientName = demande.getClient().getUser().getFirstname() +" " + demande.getClient().getUser().getLastname();
         DemandeDto demandeDto = DemandeDto.builder()
                 .id(demande.getId())
                 .description(demande.getDescription())
                 .status(String.valueOf(demande.getStatus()))
                 .date(demande.getDate()).
                 clientId(clientID).clientName(clientName)
                 .build();
         return demandeDto;
    }




    public boolean updateDemande(Long id, DemandeUpdateRequest updateRequest) {
        Optional<Demande> demandeOptional = demandeRepository.findById(id);

        if (demandeOptional.isPresent()) {
            Demande demande = demandeOptional.get();
            demande.setDescription(updateRequest.getDescription());
            demande.setType(DemandeType.valueOf(updateRequest.getTypeDemande()));
            demandeRepository.save(demande);
            return true; // Successfully updated demande
        } else {
            return false; // Demande with the given ID not found
        }
    }


   public DemandeDto findDemandeById(long id){
       Demande demande = demandeRepository.findById(id).orElse(null);
       if (demande == null) {
           return null; // Or handle the case where no demande is found
       }
       return convertToDTO(demande);
   }


    public Page<DemandeDto> getDemandesByPagination(Pageable pageable) {
        Page<Demande> demandes = demandeRepository.findAll(pageable);
        return demandes.map(this::convertToDTO);
    }

}

