package com.pfa.pfabackend.demande;

import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.client.ClientDto;
import com.pfa.pfabackend.client.ClientService;
import com.pfa.pfabackend.demande.https.req.DemandeCreateRequest;
import com.pfa.pfabackend.demande.https.req.DemandeUpdateRequest;
import com.pfa.pfabackend.demande.enums.DemandeStatus;
import com.pfa.pfabackend.demande.enums.DemandeType;
import com.pfa.pfabackend.user.UserDTO;

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
        demande.setType(DemandeType.valueOf(request.getType()));
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
                 .date(demande.getDate())
                 .type(String.valueOf(demande.getType()))
                 .clientId(clientID).clientName(clientName)
                 .build();
         return demandeDto;
    }




    public boolean updateDemande(Long id, DemandeUpdateRequest updateRequest) {
        Optional<Demande> demandeOptional = demandeRepository.findById(id);

        if (demandeOptional.isPresent()) {
            Demande demande = demandeOptional.get();
            demande.setDescription(updateRequest.getDescription());
            demande.setType(DemandeType.valueOf(updateRequest.getType()));
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
    public Page<DemandeDto> findDemandesByClient_Id(Long id,Pageable pageable) {
        Page<Demande> demandes = demandeRepository.findDemandesByClient_Id(id,pageable);
        return demandes.map(this::convertToDTO);
    }
    public boolean updateDemandeStatus(Long id, DemandeStatus newStatus) {
        Optional<Demande> demandeOptional = demandeRepository.findById(id);
        if (demandeOptional.isPresent()) {
            Demande demande = demandeOptional.get();
            demande.setStatus(newStatus);
            demandeRepository.save(demande);
            return true;
        } else {
            return false;
        }
    }
    // Update demande notif;
    public boolean updateDemandeNotif(Long id, String notif) {
        Optional<Demande> demandeOptional = demandeRepository.findById(id);
        if (demandeOptional.isPresent()) {
            Demande demande = demandeOptional.get();
            demande.setNotif(notif);
            demandeRepository.save(demande);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteDemande(long id) {
        Demande demande = demandeRepository.findById(id).orElse(null);
        if (demande != null) {
            demandeRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

