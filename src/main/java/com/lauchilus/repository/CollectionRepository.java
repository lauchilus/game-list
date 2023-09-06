package com.lauchilus.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lauchilus.entity.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {

	@Query("SELECT p FROM Collection p JOIN p.user u WHERE u.username = :username")
	List<Collection> findByUsername(String username, Pageable paginacion);



	boolean existsByUser_idAndId(Integer id, Integer id2);

}
