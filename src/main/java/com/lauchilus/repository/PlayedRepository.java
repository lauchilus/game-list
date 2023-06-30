package com.lauchilus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lauchilus.entity.Played;
import com.lauchilus.entity.User;

public interface PlayedRepository extends JpaRepository<Played, Integer> {

	@Query("SELECT p FROM Played p JOIN p.user u WHERE u.username = :username")
	Page<Played> findByUsername(String username,Pageable paginacion);

	

	
}
