package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ImageService;
import jakarta.transaction.Transactional;


@Controller
public class ImageController {
	@Autowired
	private AuthorService authorService;
	@Autowired
	private ImageService imageService;
	@Autowired 
	private BookService bookService;
	@PostMapping("/admin/upload/{type}/{id}")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			@PathVariable("type") String type,
			@PathVariable("id") Long id,
			@RequestHeader(value = "Referer", required = false) final String referer,
			Model model) {
		if (file.isEmpty()) {
			model.addAttribute("message", "File non valido");
			return "uploadResult";
		}

		try {
			byte[] imageBytes = file.getBytes();

			Image immagine=null;
			switch (type.toLowerCase()) {
			case "autore":
				this.authorService.addImageToAuthor(id, file);
				
				break;
				

			case "libro":
				this.bookService.addImageToBook(id, file);
				break;

			default:
				model.addAttribute("message", "Tipo non valido");
				return "uploadResult";
			}

			model.addAttribute("message", "Immagine salvata nel database!");
		} catch (IOException e) {
			model.addAttribute("message", "Errore durante il caricamento");
		}

		return "redirect:"+referer;
	}


	
	@GetMapping ("/admin/upload/{type}/{id}")
	public String getFormImage(@PathVariable("type") String type,@PathVariable("id") Long id,Model model) {
		model.addAttribute("type", type);
		model.addAttribute("id", id);
		return "admin/formImage";

	}
	
	@GetMapping("/image/{idI}")
	@ResponseBody
	public byte[] getImage(@PathVariable("idI") Long idI) {
		return this.imageService.getImage(idI).getImageData();
	}
}
