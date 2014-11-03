package com.mycompany.myapp.service;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.Author;
import com.mycompany.myapp.repository.AuthorRepository;

@Service
@Transactional
public class AuthorService {
	
	@Inject
    private AuthorRepository publicationRepository;
   
    public List<Author> findAll() {
    	List<Author> listpub = new ArrayList<Author>();
    	for (Author pub : publicationRepository.findAll()) {
    		listpub.add(pub);
    	}
    	return listpub;
    }
    
    public void save(Author publication) {
    	publicationRepository.save(publication);
    }
    
    public Author findOne(Long id) {
    	Author author = publicationRepository.findAuthorByID(id);
    	return author;
    }
        
    
    public void delete(Long id) {
    	publicationRepository.delete(id);
    }
    

}
