package it.uniroma3.siw.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private BookService bookService;
	
	@PostMapping("libri/{idL}/post-review")
	public String postReview(@Valid @ModelAttribute("userReview") Review userReview, BindingResult bindingResult,  @PathVariable("idL")Long idL , @RequestHeader(value = "Referer", required = false) final String referer, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("userReview ", userReview);
			
			
		}else
		{	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = credentialsService.getCredentials(userDetails.getUsername()).getUser();
			user.addRecensione(userReview);
			Book book = this.bookService.getBook(idL);
			book.removeReview(this.reviewService.getOldReview(user, book));
			book.addReview(userReview);
			this.bookService.saveBook(book);
			userReview.setUser(user);
			userReview.setData(LocalDate.now());
			this.reviewService.saveOrUpdateReview(userReview);
			System.err.println(referer);
			
		}
		
		return "redirect:"+referer;	}
	
	@GetMapping("libri/{idL}/elimina-recensione/{idR}")
	public String eliminaRecensione(@PathVariable("idL") Long idL,@PathVariable("idR") Long idR) {
		Book book = this.bookService.getBook(idL);
		Review review= this.reviewService.getReview(idR);
		review.getUser().removeReview(review);
		book.removeReview(review);
		this.bookService.saveBook(book);
		this.reviewService.deleteReview(idR);
		
		return "redirect:/libri/"+idL;
	}
	@GetMapping("/recensioni/{idL}")
    public String getCatalogoLibri(
        Model model,
        @PathVariable("idL") Long idL,
        @PageableDefault(page = 0, size = 8, sort = "data") Pageable pageable,
        @RequestParam(value = "minVoto", required = false) Integer minVoto, 
      
        @RequestParam(value = "sortBy", defaultValue = "titolo") String sortBy,
     
        @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder
    ) {
        Page<Review> reviews;

        
        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(sortOrder)) { 
            direction = Sort.Direction.DESC;
        }

      
        String sortByParam = sortBy; 

        String actualSortByProperty;
       
        switch (sortByParam) {
            case "voto":
                actualSortByProperty = "voto";
                System.err.println("DEBUG: Ordinamento per titolo: " + sortByParam);
                break;
            case "data": 
                actualSortByProperty = "data";
                System.err.println("DEBUG: Ordinamento per voti: " + sortByParam);
                break;
            
            default:
                System.err.println("DEBUG: Ordinamento predefinito p. sortByParam was: " + sortByParam);
                actualSortByProperty = "data"; 
                break;
        }

        Sort sort = Sort.by(direction, actualSortByProperty);

        
        
        
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        reviews = this.reviewService.filterReviews(minVoto, idL,sortedPageable);
        model.addAttribute("reviews", reviews);
        model.addAttribute("sortBy", actualSortByProperty); 
        model.addAttribute("sortOrder", sortOrder); 
        model.addAttribute("currentPage", reviews.getNumber());
        model.addAttribute("totalPages", reviews.getTotalPages());
        model.addAttribute("libro",this.bookService.getBook(idL));
        return "listaRecensioni.html";
    }
	
	

}
