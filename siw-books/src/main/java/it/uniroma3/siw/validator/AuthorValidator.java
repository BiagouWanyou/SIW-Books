package it.uniroma3.siw.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Author;


@Component
public class AuthorValidator implements Validator{
	@Override
	public boolean supports(Class<?> aClass) {
		return Author.class.equals(aClass);
		
	}

	@Override
	public void validate(Object target, Errors errors) {
		Author author =(Author) target;
		if (author.getDataMorte()!=null&&author.getDataNascita().isAfter(author.getDataMorte())) {
			 errors.rejectValue("dataMorte", "author.dateParadox", "Data morte prima di data nascita, risolvi il paradosso!");
		}
		
	}

}
