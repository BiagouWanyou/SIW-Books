package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.BookRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class BookService {
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	ImageService imageService;
	@Transactional
	public List<Book> getAllBooks() {
		return this.bookRepository.findAll();
		
	}
	@Transactional
	 public List<Book> getFiveBooks() {
	        // Create a Pageable object for the first page with 5 elements
	        Pageable fiveBooksPageable = PageRequest.of(0, 5); // Page 0, size 5
	        return bookRepository.findAll(fiveBooksPageable).getContent();
	    }

		
	@Transactional
	public Page<Book> filterBooks(String search, Integer minRating, String genre, Long idA, Pageable sortedPageable) {
		return this.bookRepository.filterBooks(search,minRating,genre,idA,sortedPageable);
	}
	@Transactional
	public void saveBook(@Valid Book book) {
		this.bookRepository.save(book);
	}
	@Transactional
	public Book getBook(Long id) {
		return this.bookRepository.findById(id).orElse(null);
	}
	@Transactional
	public void deleteBook(Long idL) {
		this.bookRepository.deleteById(idL);
		
	}
	@Transactional
	public List<Book> getTop6BooksByScore() {
		return this.bookRepository.findTop6ByOrderByMediaVotiDesc();
	}
	public Long countBooks() {
		return Long.valueOf(bookRepository.count());
	}
	
	@Transactional
    public void addImageToBook(Long bookId, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(bookId).orElse(null);
        if(book==null)
        	return;
                                 
        Image newImage = new Image(file.getBytes(), file.getOriginalFilename(), file.getContentType(), file.getSize());
        imageService.saveImage(newImage);
        book.addImage(newImage);
        bookRepository.save(book);
    }
	@Transactional
	public Book findBookWithImages(Long id) {
		return this.bookRepository.getEagerFetchBook(id).orElse(null);
	}
}

