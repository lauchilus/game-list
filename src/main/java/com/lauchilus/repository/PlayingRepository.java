package com.lauchilus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lauchilus.entity.Playing;

public interface PlayingRepository extends JpaRepository<Playing, Integer> {

	@Query("SELECT p FROM Playing p JOIN p.user u WHERE u.username = :username")
    Page<Playing> findByUsername(@Param("username") String username, Pageable pageable);


}
