package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zosh.model.Community;

public interface ComRepository extends JpaRepository<Community, Long>{
	
	@Query(value= "select c.* from community c order by c.created_at desc", nativeQuery = true)
	List<Community> findAllOrderByCreatedAtDesc();

}
