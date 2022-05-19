package com.webService.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface commentsrepository extends JpaRepository<comments, Integer> {
	@Query(value = "select * from comments s where s.id_entity = :entid", nativeQuery = true)
	 List<comments> findByProduct(@Param("entid") int id_entity);
	@Transactional
	@Modifying
	@Query(value = "insert into comments(id_entity,id_user,text) values (:entid,:usrid,:text)", nativeQuery = true)
	 void insertComment(@Param("entid") int id_entity,@Param("usrid") int id_user,@Param("text") String text);
	@Transactional
	@Modifying
	@Query(value = "update comments set text=:text where id=:id", nativeQuery = true)
	 void updateComment(@Param("id") int id_comment,@Param("text") String text);
}