package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional
	public Page<Author> filterAuthors(String searchNome, String searchCognome , Integer annoNascita, Integer annoMorte, Pageable sortedPageable) {
		
		return this.authorRepository.filterAuthors(searchNome, searchCognome, annoNascita, annoMorte, sortedPageable);
	}
	@Transactional
	public List<Author> getAllAuthors() {
		return this.authorRepository.findByOrderByNomeAsc();
	}
	@Transactional
	public void saveAuthor(@Valid Author author) {
		this.authorRepository.save(author);
	}
	@Transactional
	public Author getAuthor(Long id) {
		return this.authorRepository.findById(id).orElse(null);
	}
	@Transactional
	public void deleteAuthor(Long idA) {
		this.authorRepository.deleteById(idA);
		
	}
	@Transactional
	public List<Author> getTop8AuthorsByName() {
		return this.authorRepository.findTop8ByOrderByNome();
	}
	public Long countAuthors() {
		
		return Long.valueOf(this.authorRepository.count());
	}
	@Transactional
    public void addImageToAuthor(Long authorId, MultipartFile file) throws IOException {
        Author author = authorRepository.findById(authorId).orElse(null);
        if(author==null)
        	return;
                                      
        Image newImage = new Image(file.getBytes(), file.getOriginalFilename(), file.getContentType(), file.getSize());
        imageService.saveImage(newImage); // Save the image first
        author.setImmagine(newImage); // Set the new image for the author
        authorRepository.save(author); // Save the updated author
    }

}
