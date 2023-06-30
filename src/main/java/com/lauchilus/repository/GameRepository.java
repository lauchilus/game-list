package com.lauchilus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lauchilus.entity.Collection;
import com.lauchilus.entity.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {

	@Query("SELECT p FROM Game p JOIN p.collection u WHERE u.name = :collection")
	Page<Game> findByCollection(String collection, Pageable paginacion);
}
