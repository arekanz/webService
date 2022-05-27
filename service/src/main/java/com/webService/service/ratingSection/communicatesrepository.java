package com.webService.service.ratingSection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface communicatesrepository extends JpaRepository<communicates, Integer> {
	@Query(value="select * from communicates where id>:number AND id<(:number+5001)",nativeQuery=true)
	List<communicates> findByType(@Param("number") int number);
	@Transactional
	@Modifying
	@Query(value = "insert into communicates(id,text) values (:id,\"\")", nativeQuery = true)
	 void insertCommunicates(@Param("id") int id);
	@Transactional
	@Modifying
	@Query(value = "update communicates set text=:text where id=:id", nativeQuery = true)
	 void updateCommunicates(@Param("id") int id,@Param("text") String text);
}