package com.pfa.pfabackend.file.images;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ImageRepo extends JpaRepository<Image, Long> {
    
}
