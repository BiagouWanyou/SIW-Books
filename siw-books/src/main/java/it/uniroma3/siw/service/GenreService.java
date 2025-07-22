package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Genre;
import it.uniroma3.siw.repository.GenreRepository;
import jakarta.transaction.Transactional;

@Service
public class GenreService {
	@Autowired
	private GenreRepository genreRepository;
	@Transactional
	public void saveGenre(Genre genre) {
		this.genreRepository.save(genre);
	}
	@Transactional
	public List<Genre> findAllGenre() {
		return (List<Genre>) this.genreRepository.findAll();
	}

}
