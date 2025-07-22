package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
@NamedEntityGraph(
	    name = "book-with-images",
	    attributeNodes = {
	        @NamedAttributeNode("images")
	    }
	)
public class Book {
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String titolo;
	@Past
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPubblicazione;
	 private Integer numeroRecensioni;
	private Float mediaVoti;
	@NotBlank
	@Column(name = "sinossi", length = 1000)
	private String sinossi;
	@NotEmpty
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToMany(mappedBy="libri",fetch=FetchType.LAZY)
	private List<Author> autori;
	@OneToMany(mappedBy = "libro" ,cascade = CascadeType.REMOVE,orphanRemoval=true)
	private List<Review> reviews;
	@NotEmpty
	@ManyToMany
	List<Genre> generi;
	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
	private Image copertina;
	
	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id") // FK in the 'images' table, specific to products
	private Set<Image> images = new HashSet<>();

	public Set<Image> getImages() {
		return images;
	}
	public void setImages(Set<Image> images) {
		this.images = images;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public Integer getNumeroRecensioni() {
		return numeroRecensioni;
	}
	public void setNumeroRecensioni(Integer numeroRecensioni) {
		this.numeroRecensioni = numeroRecensioni;
	}
	public LocalDate getDataPubblicazione() {
		return dataPubblicazione;
	}
	public void setDataPubblicazione(LocalDate dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}
	public List<Author> getAutori() {
		return autori;
	}
	public void setAutori(List<Author> autori) {
		this.autori = autori;
	}
	public Float getMediaVoti() {
		return mediaVoti;
	}
	public void setMediaVoti(Float mediaVoti) {
		this.mediaVoti = mediaVoti;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	public String getSinossi() {
		return sinossi;
	}
	public void setSinossi(String sinossi) {
		this.sinossi = sinossi;
	}
	public List<Genre> getGeneri() {
		return generi;
	}
	public void setGeneri(List<Genre> generi) {
		this.generi = generi;
	}
	
	
	public void addImage(Image image) {
		this.images.add(image);
	}
	public Image getCopertina() {
		return copertina;
	}
	public void setCopertina(Image copertina) {
		this.copertina = copertina;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(autori, dataPubblicazione, mediaVoti, reviews, titolo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(autori, other.autori) && Objects.equals(dataPubblicazione, other.dataPubblicazione)
				&& Objects.equals(mediaVoti, other.mediaVoti) && Objects.equals(reviews, other.reviews)
				&& Objects.equals(titolo, other.titolo);
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", titolo=" + titolo + ", dataPubblicazione=" + dataPubblicazione + ", mediaVoti="
				+ mediaVoti+ "]";
	}
	public void addAuthor(Author author) {
		this.autori.add(author);
		
	}
	 public void addReview(Review newReview) {
	        if (newReview == null) return;
	        
	        if (this.reviews == null) {
	            this.reviews = new java.util.ArrayList<>();
	        }
	        this.reviews.add(newReview);
	        
	        newReview.setLibro(this);
	        
	        if (this.numeroRecensioni == null) { 
	            this.numeroRecensioni = 0;
	            this.mediaVoti = 0.0f;
	        }
	        float currentTotalScore = (this.mediaVoti != null ? this.mediaVoti : 0.0f) * this.numeroRecensioni;
	        this.numeroRecensioni++;
	        this.mediaVoti = (currentTotalScore + newReview.getVoto())/ this.numeroRecensioni;
	 }
	 public void removeReview(Review reviewToRemove) {
	        if (reviewToRemove == null || this.reviews == null || !this.reviews.contains(reviewToRemove)) {
	            return;
	        }

	        this.reviews.remove(reviewToRemove);
	        reviewToRemove.setLibro(null); // Break bidirectional link

	        if (this.numeroRecensioni != null && this.numeroRecensioni > 0) {
	            float currentTotalScore = (this.mediaVoti != null ? this.mediaVoti : 0.0f) * this.numeroRecensioni;
	            this.numeroRecensioni--;

	            if (this.numeroRecensioni == 0) {
	                this.mediaVoti = 0.0f; 
	            } else {
	                this.mediaVoti = (currentTotalScore - reviewToRemove.getVoto()) / this.numeroRecensioni;
	            }
	        }
	    }
	

}
