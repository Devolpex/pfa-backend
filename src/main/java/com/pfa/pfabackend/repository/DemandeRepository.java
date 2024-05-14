package com.pfa.pfabackend.repository;


import com.pfa.pfabackend.model.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DemandeRepository extends JpaRepository<Demande, Long> {

//    @Query("SELECT p FROM Product p ORDER BY p.id DESC")
    @Query("SELECT d FROM Demande d ORDER BY d.id DESC ")
    Page<Demande> findAll(Pageable pageable);
    Page<Demande> findDemandesByClient_Id(Long clientId, Pageable pageable);

}
