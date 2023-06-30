package com.lauchilus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lauchilus.entity.Collection;
import com.lauchilus.entity.Played;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {

	@Query("SELECT p FROM Collection p JOIN p.user u WHERE u.username = :username")
	Page<Collection> findByUsername(String username, Pageable paginacion);

	

	boolean existsByUser_idAndId(Integer id, Integer id2);

}
