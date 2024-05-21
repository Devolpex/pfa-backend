package com.pfa.pfabackend.demande;

import java.sql.Date;

import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.demande.enums.DemandeStatus;
import com.pfa.pfabackend.demande.enums.DemandeType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "demandes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private Date date;
    private String notif;

    @Enumerated(EnumType.STRING)
    private DemandeStatus status;
    
    @Enumerated(EnumType.STRING)
    private DemandeType type;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
