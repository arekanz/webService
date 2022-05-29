package com.webService.service.shopSection;

import java.sql.Date;
import java.time.LocalDate;
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

public interface newproductcontentrepository extends JpaRepository<newproductcontent, Integer> {
	 @Transactional
	 @Modifying
	 @Query(value = "insert into newproductcontent(id_newproduct,added_date) values (:id,:date)", nativeQuery = true)
		 void insertProduct(@Param("id") int id,@Param("date") LocalDate localDate);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproductcontent set content_path=:name where id_newproduct=:id", nativeQuery = true)
		 void updatecontent(@Param("id") int id,@Param("name") String content_path);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproductcontent set id_delivery=:description where id_newproduct=:id", nativeQuery = true)
		 void updatedelivery(@Param("id") int id,@Param("description") String id_delivery);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproductcontent set content_img=:img_src where id_newproduct=:id", nativeQuery = true)
		 void updatecontent_img(@Param("id") int id,@Param("img_src") String imgs);
	 @Transactional
	 @Modifying
	 @Query(value = "delete from newproductcontent where id_newproduct=:id", nativeQuery = true)
		 void deleteProduct(@Param("id") int id);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproductcontent set id_newproduct=:idtoset where id_newproduct=:id", nativeQuery = true)
		 void setMethod(@Param("id") int id,@Param("idtoset") int idtoset);
}