package com.es.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es.entity.Signup;

@Repository
public interface SignupRepository extends JpaRepository<Signup, Integer> {
	
	Signup findByEmail(String email);
	Signup findByUserName(String userName);
	
	
	

}
