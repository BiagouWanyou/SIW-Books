package it.uniroma3.siw.model;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint; // Import UniqueConstraint

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "libro_id"}))
public class Review {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String titolo;
	private String testo;
	private LocalDate data;
	private Integer voto; 
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Book libro;
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public Integer getVoto() {
		return voto;
	}
	public void setVoto(Integer voto) {
		this.voto = voto;
	}
	public Book getLibro() {
		return libro;
	}
	public void setLibro(Book libro) {
		this.libro = libro;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public int hashCode() {
		return Objects.hash(data, libro, testo, titolo, voto);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		return Objects.equals(data, other.data) && Objects.equals(libro, other.libro)
				&& Objects.equals(testo, other.testo) && Objects.equals(titolo, other.titolo)
				&& Objects.equals(voto, other.voto);
	}
	@Override
	public String toString() {
		return "Review [id=" + id + ", titolo=" + titolo + ", testo=" + testo + ", data=" + data + ", voto=" + voto
				+ ", libro=" + libro + "]";
	}
	
	
	
	
}
