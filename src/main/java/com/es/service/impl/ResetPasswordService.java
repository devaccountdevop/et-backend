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
        // Find the user by username
        Signup signup = signupRepository.findByUserName(username);

        // Check if the user exists
        if (signup != null) {
            // Check if the old password matches the stored password
            if (signup.getPassword().equals(oldPassword)) {
                // Update the password with the new password
                signup.setPassword(newPassword);
                // Save the updated user to the repository
                signupRepository.save(signup);
                return signup;
            } else {
                // Return null or throw an exception to indicate incorrect old password
                return null;
            }
        } else {
            // Return null or throw an exception to indicate user not found
            return null;
        }
    }

	

}
