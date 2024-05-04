package com.pfa.pfabackend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pfa.pfabackend.model.CodeConfirmation;

public interface CodeConfirmationRepository extends JpaRepository<CodeConfirmation, Long> {
    // @Query("SELECT cc FROM CodeConfirmation cc JOIN cc.client c JOIN c.user u WHERE u.email = :email ORDER BY cc.id DESC")
    // CodeConfirmation findLastCodeConfirmationByClientEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM CodeConfirmation cc WHERE cc.client.user.email = :email")
    void deleteCodeConfirmationsByClientEmail(@Param("email") String email);

    @Query("SELECT cc FROM CodeConfirmation cc JOIN cc.client c JOIN c.user u WHERE u.email = :email ORDER BY cc.id DESC")
    List<CodeConfirmation> findLastCodeConfirmationByClientEmail(@Param("email") String email, Pageable pageable);

}
