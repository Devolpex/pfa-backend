package com.pfa.pfabackend.user.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.pfa.pfabackend.client.Client;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "code_confirmation")
public class CodeConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private Date expiration_date;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
