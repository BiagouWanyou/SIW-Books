package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;
import jakarta.transaction.Transactional;

@Service
public class ImageService {
	@Autowired
	private ImageRepository imageRepository;
	@Transactional
	public void saveImage(Image immagine) {
		this.imageRepository.save(immagine);
		
	}
	@Transactional
	public Image getImage(Long idI) {
		return this.imageRepository.findById(idI).orElse(null);
	}
}
