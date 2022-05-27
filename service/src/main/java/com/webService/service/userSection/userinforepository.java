package com.webService.service.userSection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;



public interface userinforepository extends JpaRepository<userinfo, Integer>{
	@Transactional
	 @Modifying
	 @Query(value = "insert into userinfo(id) values (:id)", nativeQuery = true)
		 void insertUser(@Param("id") int id_entity);
	@Transactional
	 @Modifying
	 @Query(value = "update userinfo set id_visited_categories=:id_categories where id=:id", nativeQuery = true)
		 void updateUserInfo(@Param("id") int id_entity,@Param("id_categories") String id_visited_categories);
	@Transactional
	 @Modifying
	 @Query(value = "update userinfo set id_products=:id_products where id=:id", nativeQuery = true)
		 void createdpages(@Param("id") int id_entity,@Param("id_products") String id_products);
}