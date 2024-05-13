package com.pfa.pfabackend.repository;


import com.pfa.pfabackend.model.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DemandeRepository extends JpaRepository<Demande, Long> {

    Page<Demande> findAll(Pageable pageable);
    Page<Demande> findDemandesByClient_Id(Long clientId, Pageable pageable);

}
