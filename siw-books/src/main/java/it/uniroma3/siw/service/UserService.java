package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Transactional
	public void saveUser(@Valid User user) {
		this.userRepository.save(user);
	}
	@Transactional
	public Long countUsers() {
		return Long.valueOf(this.userRepository.count());
	}

}
