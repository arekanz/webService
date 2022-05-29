package com.webService.service.userSection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;



public interface previllagesrepository extends JpaRepository<previllages, Integer> {
	@Query(value = "select * from previllages s where s.id_user = :usrid", nativeQuery = true)
	 previllages findByUserId(@Param("usrid") int id_user);
	@Transactional
	 @Modifying
	 @Query(value = "insert into previllages(id_user,level) values (:id,:level)", nativeQuery = true)
		 void insertPrev(@Param("id") int id_user,@Param("level") short level);
	@Transactional
	 @Modifying
	 @Query(value = "update previllages set level=:level where id_user=:id", nativeQuery = true)
		 void updatePrev(@Param("id") int id_user,@Param("level") short level);
	@Transactional
	 @Modifying
	 @Query(value = "delete from previllages where id=:id", nativeQuery = true)
		 void deletePrev(@Param("id") int deletedid);
	@Transactional
	 @Modifying
	 @Query(value = "update previllages set id=:id where id=:lastid", nativeQuery = true)
		 void deletePrevM(@Param("lastid") int lastId,@Param("id") int deletedid);
}