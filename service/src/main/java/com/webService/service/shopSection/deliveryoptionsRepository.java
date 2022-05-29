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

public interface deliveryoptionsRepository extends JpaRepository<deliveryoptions, Integer> {
	@Transactional
	 @Modifying
	 @Query(value = "insert into deliveryoptions(id,service_name,deliver,time,price) values (:id,:service_name,:deliver,:time,:price)", nativeQuery = true)
		 void insertDeliverys(@Param("id") int id,@Param("service_name") String name,@Param("deliver") String deliver,@Param("time") int time,@Param("price") float price);
	@Transactional
	 @Modifying
	 @Query(value = "update deliveryoptions set service_name=:service_name,deliver=:deliver,time=:time,price=:price where id=:id", nativeQuery = true)
		 void updateDeliverys(@Param("id") int id,@Param("service_name") String name,@Param("deliver") String deliver,@Param("time") int time,@Param("price") float price);
	 @Transactional
	 @Modifying
	 @Query(value = "delete from deliveryoptions where id=:id", nativeQuery = true)
		 void deleteDeliverys(@Param("id") int deletedid);
}