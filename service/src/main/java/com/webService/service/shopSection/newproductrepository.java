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

public interface newproductrepository extends JpaRepository<newproduct, Integer> {
	@Query(value = "select * from newproduct s where s.id_user=:id_user", nativeQuery = true)
	 List<newproduct> findByUserId(@Param("id_user") int id_user);
	 @Query(value = "select * from newproduct s where s.reserved_id=:res", nativeQuery = true)
	 newproduct findByReservedId(@Param("res") int resersed_id);
	 @Transactional
	 @Modifying
	 @Query(value = "insert into newproduct(id_user,reserved_id) values (:id_user,:id_reserved)", nativeQuery = true)
		 void insertProduct(@Param("id_user") int id_user,@Param("id_reserved") int id_reserved);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproduct set name=:name where id=:id", nativeQuery = true)
		 void updatename(@Param("id") int id,@Param("name") String name);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproduct set description=:description where id=:id", nativeQuery = true)
		 void updatedescription(@Param("id") int id,@Param("description") String description);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproduct set img_src=:img_src where id=:id", nativeQuery = true)
		 void updateimg_src(@Param("id") int id,@Param("img_src") String name);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproduct set id_category=:category where id=:id", nativeQuery = true)
		 void updatecategory(@Param("id") int id,@Param("category") int id_category);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproduct set price=:price where id=:id", nativeQuery = true)
		 void updateprice(@Param("id") int id,@Param("price") float price);
	 @Transactional
	 @Modifying
	 @Query(value = "delete from newproduct where id=:id", nativeQuery = true)
		 void deleteProduct(@Param("id") int id);
	 @Transactional
	 @Modifying
	 @Query(value = "update newproduct set id=:idtoset,reserved_id=:lastreserved where id=:id", nativeQuery = true)
		 void setMethod(@Param("id") int id,@Param("idtoset") int idtoset,@Param("lastreserved") int reserved);
}