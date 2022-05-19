package com.webService.service.shopSection;

import java.util.List;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository

public interface shopentityrepository extends JpaRepository<shopentity, Integer> {
	@Query(value = "select * from shopentity s where s.id_category like :category_id% and (s.name like %:keyword% or s.description like %:keyword%)", nativeQuery = true)
	 List<shopentity> findByAttributes(@Param("keyword") String keyword,@Param("category_id") String categoryID);
}