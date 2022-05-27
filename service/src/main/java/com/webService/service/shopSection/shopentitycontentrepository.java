package com.webService.service.shopSection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface shopentitycontentrepository extends JpaRepository<shopentitycontent, Integer> {
	 @Transactional
	 @Modifying
	 @Query(value = "insert into shopentitycontent(id_entity,id_delivery,content_path) values (:id,:del,:conpath)", nativeQuery = true)
		 void insertProductCon(@Param("id") int id_entity,@Param("del") String id_delivery,@Param("conpath") String content_path);

}