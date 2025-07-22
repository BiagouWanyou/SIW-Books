package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;




@Controller
public class AuthController {
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	@Autowired
	private ReviewService reviewService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
	
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "register";
	}
	
	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult userBindingResult, @Valid
			@ModelAttribute("credentials") Credentials credentials,
			BindingResult credentialsBindingResult,
			Model model) {
		
		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			
			userService.saveUser(user);
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			model.addAttribute("user", user);
			return "redirect:/login";
		}
	 
		return "register";
	}
	@GetMapping("/")
	public String getHomePage(Model model) {
		model.addAttribute("totalLibri", this.bookService.countBooks());
		model.addAttribute("totalAutori", this.authorService.countAuthors());
		model.addAttribute("totalRecensioni", this.reviewService.countReviews());
		model.addAttribute("totalUtenti", this.userService.countUsers());

		model.addAttribute("autori", this.authorService.getTop8AuthorsByName() );
		model.addAttribute("books",this.bookService.getTop6BooksByScore());
		return "home.html";
	}
}
