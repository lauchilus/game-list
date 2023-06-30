package com.lauchilus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.lauchilus.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

//	UserDetails findByUsername(String username);
	User findByUsername(String username);
	
	User save(User user);

	boolean existsByUsername(String username);

	User getReferenceByUsername(String username);

}
