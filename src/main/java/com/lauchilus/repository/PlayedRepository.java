package com.lauchilus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lauchilus.entity.Played;

public interface PlayedRepository extends JpaRepository<Played, Integer> {

	@Query("SELECT p FROM Played p JOIN p.user u WHERE u.username = :username")
	List<Played> findByUsername(String username);




}
