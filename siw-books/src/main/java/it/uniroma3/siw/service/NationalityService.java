package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Nationality;
import it.uniroma3.siw.repository.NationalityRepository;
import jakarta.transaction.Transactional;

@Service
public class NationalityService {
	@Autowired
	private NationalityRepository nationatlityRepository;
	@Transactional
	public List<Nationality>getAllNationalities() {
		return (List<Nationality>) this.nationatlityRepository.findAll();
	}

}
