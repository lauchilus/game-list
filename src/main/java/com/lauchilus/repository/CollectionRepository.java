package com.lauchilus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lauchilus.entity.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {

}
