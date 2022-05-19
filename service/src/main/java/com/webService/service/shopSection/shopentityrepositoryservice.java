/*package com.webService.service.shopSection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class shopentityrepositoryservice {
 @Autowired
 private shopentityrepository repository;
 

 public List<shopentity> getAllProducts(){
  List<shopentity> list =  (List<shopentity>)repository.findAll();
  return list;
 }
 
 public List<shopentity> getByAttributes(String keyword,String catId){
  return repository.findByAttributes(keyword,catID);
 }
}*/