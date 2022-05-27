package com.webService.service.userSection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;



public interface userrepository extends JpaRepository<user, Integer>{
	@Query(value = "select * from user s where s.id = :entid", nativeQuery = true)
	user getUserById(@Param("entid") int id_entity);
	@Query(value = "select * from user s where s.login = :login", nativeQuery = true)
	user getUserByLogin(@Param("login") String login);
	@Transactional
	@Modifying
	@Query(value = "insert into user(login,password,email) values (:login,:password,:email)", nativeQuery = true)
	 void insertUser(@Param("login") String login,@Param("password") String password,@Param("email") String email);
	@Transactional
	@Modifying
	@Query(value = "update user set email=:email where id=:id", nativeQuery = true)
	 void changeEmail(@Param("id") int id,@Param("email") String email);
	@Transactional
	@Modifying
	@Query(value = "update user set password=:password where id=:id", nativeQuery = true)
	 void changePass(@Param("id") int id,@Param("password") String password);
}