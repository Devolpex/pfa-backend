package com.pfa.pfabackend.client;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.pfa.pfabackend.auth.enums.Auth;
import com.pfa.pfabackend.demande.Demande;
import com.pfa.pfabackend.user.models.CodeConfirmation;
import com.pfa.pfabackend.user.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "clients")
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Auth auth;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client" ,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CodeConfirmation> codeConfirmationList;

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Demande> demandeList;
}
