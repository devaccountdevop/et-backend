 package com.es.service.impl;
 
  import java.util.Optional; import java.util.UUID;

  import org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.stereotype.Service;
  
  import com.es.entity.ForgotPassword; import
  com.es.repository.ForgotPasswordRepository; import
  com.es.service.ForgotPasswordService;
  
  @Service public class ForgotPasswordServiceImpl implements
  ForgotPasswordService {
  
  @Autowired private ForgotPasswordRepository forgotPasswordRepository;
  
  @Override public Optional<ForgotPassword> findByEmail(String email)
  {
return forgotPasswordRepository.findByEmail(email); }
  
  @Override public void generatePasswordResetToken(ForgotPassword
  forgotPassword) { String resetToken = UUID.randomUUID().toString();
  forgotPassword.setResetToken(resetToken);
  forgotPasswordRepository.save(forgotPassword); }
  
  @Override public boolean isValidResetToken(String resetToken) {
  Optional<ForgotPassword> userOptional =
  forgotPasswordRepository.findByResetToken(resetToken); return
  userOptional.isPresent(); }
  
  @Override public void resetPassword(ForgotPassword forgotPassword, String
  newPassword) { forgotPassword.setPassword(newPassword);
  forgotPassword.setResetToken(null);
  forgotPasswordRepository.save(forgotPassword); } }
  