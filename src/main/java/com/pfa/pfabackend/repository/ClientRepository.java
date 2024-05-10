package com.pfa.pfabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pfa.pfabackend.enums.Auth;
import com.pfa.pfabackend.model.Client;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query("SELECT c.auth FROM Client c WHERE c.user.email = :email")
    Auth findAuthByEmail(@Param("email")String email);
    Client findByUserEmail(String email);
    Page<Client> findAll(Pageable pageable);
    @Query("SELECT c FROM Client c WHERE c.user.email LIKE %:search% OR c.user.firstname LIKE %:search% OR c.user.lastname LIKE %:search%")
    Page<Client> findByEmailOrFirstNameOrLastName(String search, Pageable pageable);

    Client findByUserId(Long id);


}
