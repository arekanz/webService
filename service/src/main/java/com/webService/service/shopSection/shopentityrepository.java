package com.webService.service.shopSection;

import java.util.List;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//import java.util.List;

@Repository

public interface shopentityrepository extends JpaRepository<shopentity, Integer> {
	@Query(value = "select * from shopentity s where s.id_category like :category_id% and (s.name like %:keyword% or s.description like %:keyword%)", nativeQuery = true)
	 List<shopentity> findByAttributes(@Param("keyword") String keyword,@Param("category_id") String categoryID);
	 @Transactional
	 @Modifying
	 @Query(value = "insert into shopentity(id,name,description,img_src,id_category,price) values (:id,:name,:description,:img_src,:id_category,:price)", nativeQuery = true)
		 void insertProduct(@Param("id") int id_entity,@Param("name") String name,@Param("description") String description,@Param("img_src") String img_src,@Param("id_category") int id_category,@Param("price") float price);
	 @Query(value = "select id,name,description,img_src,id_category,price from shopentity s inner join shopentityinfo sf on s.id = sf.id_entity where s.id_category like :category_id% order by sf.views desc limit 50", nativeQuery = true)
	 List<shopentity> getMostVisited(@Param("category_id") String categoryID);
}