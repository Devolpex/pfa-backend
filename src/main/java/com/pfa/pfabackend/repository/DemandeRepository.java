package com.pfa.pfabackend.repository;

import com.pfa.pfabackend.model.Client;
import com.pfa.pfabackend.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DemandeRepository extends JpaRepository<Demande, Long> {



}
