package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	@Past
	@NotNull
	private LocalDate dataNascita;
	private LocalDate dataMorte;
	@NotNull
	@ManyToOne
	private Nationality nazionalita;
	

	@ManyToMany(fetch = FetchType.LAZY)
	private List<Book> libri;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Image immagine;

	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public LocalDate getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}
	public LocalDate getDataMorte() {
		return dataMorte;
	}
	public void setDataMorte(LocalDate dataMorte) {
		this.dataMorte = dataMorte;
	}
	
	public Nationality getNazionalita() {
		return nazionalita;
	}
	public void setNazionalita(Nationality nazionalita) {
		this.nazionalita = nazionalita;
	}
	public List<Book> getLibri() {
		return libri;
	}
	public void setLibri(List<Book> books) {
		this.libri = books;
	}
	
	
	
	public Image getImmagine() {
		return immagine;
	}
	public void setImmagine(Image immagine) {
		this.immagine = immagine;
	}
	@Override
	public int hashCode() {
		return Objects.hash(libri, cognome, dataMorte, dataNascita, nazionalita, nome);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(libri, other.libri) && Objects.equals(cognome, other.cognome)
				&& Objects.equals(dataMorte, other.dataMorte) && Objects.equals(dataNascita, other.dataNascita)
				&& Objects.equals(nazionalita, other.nazionalita) && Objects.equals(nome, other.nome);
	}
	@Override
	public String toString() {
		return "Author [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", dataNascita=" + dataNascita
				+ ", dataMorte=" + dataMorte + ", nazionalita=" + nazionalita + ", books=" + libri + "]";
	}



}
