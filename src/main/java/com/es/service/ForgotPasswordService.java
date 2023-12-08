package com.es.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.es.entity.ForgotPassword;


public interface ForgotPasswordService {

	Optional<ForgotPassword> findByEmail(String email);

	void generatePasswordResetToken(ForgotPassword forgotPassword);

	boolean isValidResetToken(String resetToken);

	void resetPassword(ForgotPassword forgotPassword, String newPassword);
}
