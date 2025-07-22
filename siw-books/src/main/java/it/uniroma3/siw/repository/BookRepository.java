package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {



	@Query(value = "SELECT DISTINCT b FROM Book b " + // DISTINCT to avoid duplicates from the join
			"LEFT JOIN b.generi g " +
			"LEFT JOIN b.autori a "+
			"WHERE (:search IS NULL OR LOWER(b.titolo) LIKE LOWER(CONCAT('%', FUNCTION('TEXT', :search), '%'))) " + /// Search by title OR author
			"AND (:minRating IS NULL OR b.mediaVoti >= :minRating) " + 
			"AND (:idA IS NULL OR a.id = :idA) " + 
			"AND (:genre IS NULL OR g.name = :genre)" // Filter by a single genre name
			)
	Page<Book> filterBooks(
			@Param("search") String search,
			@Param("minRating") Integer minRating, // Assuming mediaVoti is comparable to Integer
			@Param("genre") String genre,
			@Param("idA") Long idA,
			Pageable sortedPageable);

	List<Book> findTop6ByOrderByMediaVotiDesc(); 
	@Query("SELECT b FROM Book b WHERE b.id = :id")
    @EntityGraph(value = "book-with-images", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> getEagerFetchBook(@Param("id")Long id);
}
