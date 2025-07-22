package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class CredentialsService {
	@Autowired
    protected PasswordEncoder passwordEncoder;
	@Autowired
	CredentialsRepository credentialsRepository;
	@Transactional
	public void saveCredentials(@Valid Credentials credentials) {
		 credentials.setRole(Credentials.DEFAULT_ROLE);
	        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
	      this.credentialsRepository.save(credentials);
	}
	@Transactional
	public Credentials getCredentials(String username) {
		return this.credentialsRepository.findByUsername(username);
	}
}
