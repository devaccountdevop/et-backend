package com.es.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.entity.Signup;
import com.es.repository.SignupRepository;

@Service
public class ResetPasswordService {

	private final SignupRepository signupRepository;

	@Autowired
	public ResetPasswordService(SignupRepository signupRepository) {
		this.signupRepository = signupRepository;
	}

	public Signup resetPassword(String username, String oldPassword, String newPassword) {

		Signup signup = signupRepository.findByUserName(username);

		if (signup != null) {

			if (signup.getPassword().equals(oldPassword)) {

				signup.setPassword(newPassword);

				signupRepository.save(signup);
				return signup;
			} else {

				return null;
			}
		} else {

			return null;
		}
	}

	public Signup resetPasswordByMail(String username, String newPassword) {
		Signup signup = signupRepository.findByUserName(username);

		if (signup != null) {

			signup.setPassword(newPassword);

			signupRepository.save(signup);
			return signup;
		} else {

			return null;
		}

	}

	public Signup getUserByUsername(String username) {
		return signupRepository.findByUserName(username);
	}

}
