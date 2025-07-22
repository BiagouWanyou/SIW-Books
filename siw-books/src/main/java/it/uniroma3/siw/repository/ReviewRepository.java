package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findTop3ByOrderByData();
	Optional<Review> findByUserAndLibro(User user, Book libro);
	
	@Query(value = "SELECT DISTINCT r FROM Review r " + 
			"WHERE (:minVoto IS NULL OR r.voto >= :minVoto) " + 
			"AND ( r.libro.id = :idL)" 
			)
	Page<Review> filterReviews(Integer minVoto, Long idL, Pageable sortedPageable);
	List<Review> findTop3ByLibroIdOrderByDataAsc(Long id);

}
