package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.GenreService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Controller
public class BookController {
	@Autowired 
	private BookService bookService;
	@Autowired
	private GenreService genreService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private AuthorService authorservice;
	@Autowired
	private ReviewService reviewService;

	@GetMapping("/libri")
    public String getCatalogoLibri(
        Model model,
        
        @PageableDefault(page = 0, size = 8, sort = "titolo") Pageable pageable,
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "minRating", required = false) Integer minRating, 
      
        @RequestParam(value = "sortBy", defaultValue = "titolo") String sortBy,
     
        @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
        @RequestParam(value = "genre", required=false) String genre,
        @RequestParam(value = "autore", required=false) Author autore
    ) {
        Page<Book> libri;

        
        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(sortOrder)) { 
            direction = Sort.Direction.DESC;
        }

      
        String sortByParam = sortBy; 

        String actualSortByProperty;
       
        switch (sortByParam) {
            case "titolo":
                actualSortByProperty = "titolo";
                System.err.println("DEBUG: Ordinamento per titolo: " + sortByParam);
                break;
            case "mediaVoti": 
                actualSortByProperty = "mediaVoti";
                System.err.println("DEBUG: Ordinamento per voti: " + sortByParam);
                break;
            case "numReviews": 
                actualSortByProperty = "numReviews";
                System.err.println("DEBUG: Ordinamento per recensioni: " + sortByParam);
                break;
            default:
                System.err.println("DEBUG: Ordinamento predefinito per id. sortByParam was: " + sortByParam);
                actualSortByProperty = "id"; 
                break;
        }

        Sort sort = Sort.by(direction, actualSortByProperty);

        if (search == null || search.trim().isEmpty()) {
            search = null;
        }
        if (genre==null || genre.trim().isEmpty())
        	genre=null;
        Long idA=null;
        if(autore!=null)
         idA= autore.getId();
        
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        libri = this.bookService.filterBooks(search, minRating,genre, idA, sortedPageable);
        model.addAttribute("authors", this.authorservice.getAllAuthors());
        model.addAttribute("libri", libri);
        model.addAttribute("autore", idA);
        model.addAttribute("search", search);
        model.addAttribute("minRating", minRating);
        model.addAttribute("generi", this.genreService.findAllGenre());
        model.addAttribute("genre",genre);
        model.addAttribute("sortBy", actualSortByProperty); 
        model.addAttribute("sortOrder", sortOrder); 
        model.addAttribute("currentPage", libri.getNumber());
        model.addAttribute("totalPages", libri.getTotalPages());

        return "catalogoLibri.html";
    }
	
	@GetMapping("admin/registra-libro")
	public String registraLibro(Model model) {
		model.addAttribute("AllGenres",this.genreService.findAllGenre());
		model.addAttribute("book",new Book());
		model.addAttribute("autori", this.authorservice.getAllAuthors());
		return "admin/formBook.html";
	}
	
	@PostMapping("admin/registra-libro")
	public String salvaLibro (@Valid @ModelAttribute("book") Book book,
	BindingResult bindingResult,
	Model model, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
		if (bindingResult.hasErrors()) {
			model.addAttribute("AllGenres", this.genreService.findAllGenre());
			return "admin/formBook.html";
		}
		
		if (file != null && !file.isEmpty()) {
	     Image immagine =new Image(file.getBytes(),file.getName(),file.getContentType(),file.getSize());
	     this.imageService.saveImage(immagine);
	     book.setCopertina(immagine);
	  
	    }
		this.bookService.saveBook(book);
		
		return "redirect:/libri/"+book.getId();
	}
	
	@GetMapping("libri/{id}")
	public String getReviews(@PathVariable("id") Long id ,Model model) {
		Book book = this.bookService.findBookWithImages(id);
		if(book==null)
			return "redirect:/";
		List<Review> top3ByDate = this.reviewService.findTop3ByDate(id);
		model.addAttribute("reviews", top3ByDate);
		model.addAttribute("libro", book);
		model.addAttribute("images", book.getImages());
		model.addAttribute("userReview", new Review());
		if(book.getAutori().isEmpty())
			System.err.println("VUOTO");
		return "libroDettaglio.html";
	}
	@GetMapping("admin/modifica-libro/{idL}")
	public String modificaLibro(@PathVariable("idL") Long  idL, Model model) {
		model.addAttribute("AllGenres",this.genreService.findAllGenre());
		Book book = this.bookService.findBookWithImages(idL);
		if(book==null)
			return "redirect:/";
		System.err.println(book.getDataPubblicazione().toString());
		model.addAttribute("book",book);
		model.addAttribute("autori", this.authorservice.getAllAuthors());
		
		return "admin/formBook.html";
	}
	@GetMapping("admin/elimina-libro/{idL}")
	public String eliminaLibro(@PathVariable("idL") Long idL, Model model) {
		this.bookService.deleteBook( idL);
		return "redirect:/";
	}
	
}
