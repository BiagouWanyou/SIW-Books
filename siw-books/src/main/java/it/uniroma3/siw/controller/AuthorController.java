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
import org.springframework.data.domain.PageRequest;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.GenreService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.NationalityService;
import it.uniroma3.siw.validator.AuthorValidator;
import jakarta.validation.Valid;

@Controller
public class AuthorController {
	@Autowired
	AuthorValidator authorValidator;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private NationalityService nationalityService;
	@Autowired
	private ImageService imageService;
	
	@GetMapping("/autori")
    public String getCatalogoLibri(
        Model model,
        // (1) Changed default sort to 'titolo' for consistency
        @PageableDefault(page = 0, size = 8, sort = "cognome") Pageable pageable,
        @RequestParam(value = "searchNome", required = false) String searchNome,
        @RequestParam(value = "searchCognome", required = false) String searchCognome,
        @RequestParam(value = "annoNascita", required = false) Integer annoNascita, // Changed to Float as per your Book entity's mediaVoti
        @RequestParam(value = "annoMorte", required=false) Integer annoMorte,
        @RequestParam(value = "sortBy", defaultValue = "cognome") String sortBy,
        @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder
 
    ) {
        

        // (4) Corrected check for sort direction to match "asc"/"desc" from Thymeleaf
        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(sortOrder)) { // Use "desc" (lowercase) from Thymeleaf
            direction = Sort.Direction.DESC;
        }

        // sortByParam now defaults to "titolo" if not provided by request
        String sortByParam = sortBy; // No need for null check if defaultValue is set

        String actualSortByProperty;
        // Using Option B for matching: Thymeleaf values are exact entity field names,
        // and switch matches those. Remove .toLowerCase() for cleaner match.
        switch (sortByParam) {
            case "nome":
                actualSortByProperty = "nome";
                System.err.println("DEBUG: Ordinamento per nome: " + sortByParam);
                break;
            case "dataNascita": // Matches Thymeleaf value "mediaVoti" and entity field name
                actualSortByProperty = "dataNascita";
                System.err.println("DEBUG: Ordinamento per nascita: " + sortByParam);
                break;
            case "dataMorte": // Matches Thymeleaf value "numReviews" and assumed entity field name
                actualSortByProperty = "dataMorte";
                System.err.println("DEBUG: Ordinamento per morte: " + sortByParam);
                break;
            default:
                System.err.println("DEBUG: Ordinamento predefinito per id. sortByParam was: " + sortByParam);
                actualSortByProperty = "nome"; // Default sort property for safety
                break;
        }

        Sort sort = Sort.by(direction, actualSortByProperty);

        if (searchNome == null || searchNome.trim().isEmpty()) {
            searchNome = null;
        }
        if (searchCognome == null || searchCognome.trim().isEmpty()) {
        	searchCognome = null;
        }
        
        
        // (7) CRITICAL FIX: Create new Pageable with the correct sort and pass it to service
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        //libri = this.bookService.searchBooks(search, minRating, sortedPageable); // <--- NOW PASSING sortedPageable
        Page<Author> autori = this.authorService.filterAuthors(searchNome, searchCognome, annoNascita,annoMorte, sortedPageable);
        model.addAttribute("autori", autori);
        model.addAttribute("searchNome", searchNome);
        model.addAttribute("searchCognome", searchCognome);
        model.addAttribute("annoNascita", annoNascita);
        model.addAttribute("annoMorte",annoMorte);
        model.addAttribute("sortBy", actualSortByProperty); // For th:selected in UI
        model.addAttribute("sortOrder", sortOrder); // Keep original sortOrder for th:selected
        model.addAttribute("currentPage", autori.getNumber());
        model.addAttribute("totalPages", autori.getTotalPages());

        return "catalogoAutori.html";
    }
	@GetMapping("admin/registra-autore")
	public String registraLibro(Model model) {
		model.addAttribute("AllNationalities",this.nationalityService.getAllNationalities());
		model.addAttribute("author",new Author());
		return "admin/formAuthor.html";
	}
	
	@PostMapping("admin/registra-autore")
	public String salvaLibro (@Valid @ModelAttribute("author") Author author,
	BindingResult bindingResult,
	Model model, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException{
		this.authorValidator.validate(author, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("AllNationalities",this.nationalityService.getAllNationalities());
			return "admin/formAuthor.html";
		}
		
		if (file != null && !file.isEmpty()) {
	     Image immagine =new Image(file.getBytes(),file.getName(),file.getContentType(),file.getSize());
	     this.imageService.saveImage(immagine);
	     author.setImmagine(immagine);

	    }
		this.authorService.saveAuthor(author);
		
		return "redirect:/";
	}
	@GetMapping("autori/{id}")
	public String getReviews(@PathVariable("id") Long id ,Model model) {
		Author author = this.authorService.getAuthor(id);
		if(author==null)
			return "redirect:/";
		
		model.addAttribute("autore", author);
		
		return "autoreDettaglio.html";
	}
	@GetMapping("admin/modifica-autore/{idA}")
	public String modificaAutore(@PathVariable("idA") Long  idA, Model model) {
		Author author = this.authorService.getAuthor(idA);
		if(author==null)
			return "redirect:/";
		model.addAttribute("AllNationalities",this.nationalityService.getAllNationalities());
		model.addAttribute("author",author);
		
		return "admin/formAuthor.html";
	}
	@GetMapping("admin/elimina-autore/{idA}")
	public String eliminaLibro(@PathVariable("idA") Long idA, Model model) {
		this.authorService.deleteAuthor( idA);
		return "redirect:/";
	}
}
