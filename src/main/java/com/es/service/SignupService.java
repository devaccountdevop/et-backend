package com.es.service;

import java.util.List;

import com.es.entity.Signup;

public interface SignupService {

	
	Signup getUserById(int id);
	Signup getUserByEmail(String email);
	Signup getUserByUserName(String userName);
	Signup saveUser(Signup signup);
	List<Signup> Userlist(int id);
	void deleteUser(int id);
	
}
