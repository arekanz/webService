package com.webService.service.shopSection;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface shopentityinforepository extends JpaRepository<shopentityinfo, Integer> {
	@Transactional
	 @Modifying
	 @Query(value = "insert into shopentityinfo(id_entity,creating_date,min_delivery_cost) values (:id,:date,:cost)", nativeQuery = true)
		 void insertProductInfo(@Param("id") int id_entity,@Param("date") LocalDate date,@Param("cost") float min_del_cost);

}