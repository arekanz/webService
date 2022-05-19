package com.webService.service.shopSection;

import org.springframework.data.jpa.repository.JpaRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface shopentitycontentrepository extends JpaRepository<shopentitycontent, Integer> {

}