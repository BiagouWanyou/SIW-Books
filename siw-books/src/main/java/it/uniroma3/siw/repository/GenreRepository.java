package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {

}
