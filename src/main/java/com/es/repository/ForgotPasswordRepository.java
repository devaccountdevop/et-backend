package com.es.repository;

	import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es.entity.ForgotPassword;
    import java.util.Optional;
 
    @Repository
	public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {
	    Optional<ForgotPassword> findByEmail(String email);
	    Optional<ForgotPassword> findByResetToken(String resetToken);
	}

