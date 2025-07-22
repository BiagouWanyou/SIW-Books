package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	@Transactional
	public List<Review> findTop3ByDate(Long id) {
		return this.reviewRepository.findTop3ByLibroIdOrderByDataAsc(id);
	}
	@Transactional
	public void saveReview(@Valid Review userReview) {
		this.reviewRepository.save(userReview);
		
	}
	@Transactional
	public void saveOrUpdateReview( Review newReview) {
		Review oldReview= this.reviewRepository.findByUserAndLibro(newReview.getUser(),newReview.getLibro()).orElse(null);
		if(oldReview!=null)
			newReview.setId(oldReview.getId());
		this.reviewRepository.save(newReview);
		
	}
	@Transactional
	public void deleteReview(Long idR) {
		this.reviewRepository.deleteById(idR);
		
	}
	@Transactional
	public Review getReview(Long idR) {
		return this.reviewRepository.findById(idR).orElse(null);
	}
	@Transactional
	public Page<Review> filterReviews(Integer minVoto, Long idL,Pageable sortedPageable) {
		return this.reviewRepository.filterReviews(minVoto, idL,sortedPageable);
	}
	public Long countReviews() {
		return Long.valueOf(this.reviewRepository.count());
	}

	
}
