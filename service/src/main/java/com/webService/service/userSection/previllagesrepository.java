package com.webService.service.userSection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface previllagesrepository extends JpaRepository<previllages, Integer> {
	@Query(value = "select * from previllages s where s.id_user = :usrid", nativeQuery = true)
	 previllages findByUserId(@Param("usrid") int id_user);
}