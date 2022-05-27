package com.webService.service.ratingSection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface reportsrepository extends JpaRepository<reports, Integer> {
	@Query(value="select * from reports where id_reason>:number AND id_reason<(:number+5001) order by report_date desc",nativeQuery=true)
	List<reports> findByType(@Param("number") int number);
}