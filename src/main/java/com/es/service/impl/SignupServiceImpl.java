package com.es.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.Signup;
import com.es.repository.SignupRepository;
import com.es.service.SignupService;

@Service
public class SignupServiceImpl implements SignupService {

	@Autowired
	SignupRepository signupRepository;

	@Override
	public Signup getUserById(int id) {
		Optional<Signup> list = signupRepository.findById(id);
		return !list.isPresent() ? null : list.get();
	}

	@Override
	public Signup getUserByEmail(String email) {
		Optional<Signup> list = Optional.of(signupRepository.findByEmail(email));
		return !list.isPresent() ? null : list.get();
	}

	@Override
	public Signup getUserByUserName(String userName) {
		Optional<Signup> optionalUser = Optional.ofNullable(signupRepository.findByUserName(userName));
		return optionalUser.orElse(null);
	}

	@Override
	public Signup saveUser(Signup signup) {
		return this.signupRepository.save(signup);
	}

	@Override
	public List<Signup> Userlist(int id) {
		return this.signupRepository.findAll();
	}

	@Override
	public void deleteUser(int id) {
		this.signupRepository.deleteById(id);
	}

	@Override
	public Signup findUserByEmailOrUsername(String email, String userName) {
		Signup existingUser = this.signupRepository.findUserByEmailOrUserName(email, userName);

		if (existingUser != null) {
			return existingUser;
		}

		return null;
	}
}
