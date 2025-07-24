package it.uniroma3.siw.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator {

 @Autowired
 private CredentialsService credentialsService;

 @Override
 public boolean supports(Class<?> clazz) {
     return Credentials.class.equals(clazz);
 }

 @Override
 public void validate(Object target, Errors errors) {
     Credentials credentials = (Credentials) target;
     String username = credentials.getUsername();

     // Check if username already exists
     if (username != null && credentialsService.existsByUsername(username) != false) {
         errors.rejectValue("username", "duplicate.credentials.username", "This username is already taken.");
     }
 }
}
