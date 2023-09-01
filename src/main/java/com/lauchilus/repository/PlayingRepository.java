package com.lauchilus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lauchilus.entity.Playing;

public interface PlayingRepository extends JpaRepository<Playing, Integer> {

	@Query("SELECT p FROM Playing p JOIN p.user u WHERE u.username = :username")
    List<Playing> findByUsername(@Param("username") String username);

	boolean existsByUser_idAndId(Integer id, Integer playingId);


}
