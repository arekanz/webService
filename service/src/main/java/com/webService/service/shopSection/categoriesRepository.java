package com.webService.service.shopSection;

import java.util.List;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.webService.service.userSection.previllages;

//import java.util.List;

@Repository

public interface categoriesRepository extends JpaRepository<categories, Integer> {
	@Query(value = "select * from categories c where c.id like :category_id%", nativeQuery = true)
	 List<categories> findByAttributes(@Param("category_id") String categoryID);
	@Transactional
	 @Modifying
	 @Query(value = "insert into categories(id,name) values (:id,:name)", nativeQuery = true)
		 void insertcategories(@Param("id") int id,@Param("name") String name);
	@Transactional
	 @Modifying
	 @Query(value = "update categories set name=:name where id=:id", nativeQuery = true)
		 void updatecategories(@Param("id") int id,@Param("name") String name);
	 @Transactional
	 @Modifying
	 @Query(value = "delete from categories where id like :id%", nativeQuery = true)
		 void deletecategories(@Param("id") int deletedid);
}