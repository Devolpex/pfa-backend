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
    @Query("SELECT d FROM Demande d ORDER BY d.id DESC ")
    Page<Demande> findDemandesByClient_Id(Long clientId, Pageable pageable);
    @Query("SELECT d FROM Demande d WHERE d.description LIKE %?1% OR str(d.type) LIKE %?1% OR str(d.status) LIKE %?1% ORDER BY d.id DESC")
    Page<Demande> searchDemandes(String search, Pageable pageable);


}
