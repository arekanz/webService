package com.webService.service.shopSection;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository

public interface deliveryoptionsRepository extends JpaRepository<deliveryoptions, Integer> {

}