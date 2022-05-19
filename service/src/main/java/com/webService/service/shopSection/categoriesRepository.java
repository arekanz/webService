package com.webService.service.shopSection;

import java.util.List;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository

public interface categoriesRepository extends JpaRepository<categories, Integer> {
	@Query(value = "select * from categories c where c.id like :category_id%", nativeQuery = true)
	 List<categories> findByAttributes(@Param("category_id") String categoryID);
}