package com.lauchilus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lauchilus.entity.Played;

public interface PlayedRepository extends JpaRepository<Played, Integer> {

}
