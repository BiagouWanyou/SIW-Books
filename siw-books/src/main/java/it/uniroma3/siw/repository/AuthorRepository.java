package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	@Query(value = "SELECT DISTINCT a FROM Author a " + // DISTINCT to avoid duplicates from the join
			"WHERE (:searchNome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', FUNCTION('TEXT', :searchNome), '%'))) "+
			"AND (:searchCognome IS NULL OR LOWER(a.cognome) LIKE LOWER(CONCAT('%', FUNCTION('TEXT', :searchCognome), '%')))" + /// Search by title OR author
			"AND (:annoNascita IS NULL OR YEAR(a.dataNascita) = :annoNascita) " + // Filter by minimum rating
			"AND (:annoMorte IS NULL OR YEAR(a.dataMorte) = :annoMorte) " // Filter by a single genre name
			)
	
	Page<Author> filterAuthors(@Param("searchNome")String searchNome, @Param("searchCognome")String searchCognome, @Param("annoNascita")Integer annoNascita, @Param("annoMorte")Integer annoMorte, Pageable sortedPageable);

	public List<Author> findTop8ByOrderByNome();

	List<Author> findByOrderByNomeAsc();

}
