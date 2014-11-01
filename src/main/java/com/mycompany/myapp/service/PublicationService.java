package com.mycompany.myapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.Publication;
import com.mycompany.myapp.repository.PublicationRepository;

@Service
@Transactional
public class PublicationService {
	
	@Inject
    private PublicationRepository publicationRepository;
   
    public List<Publication> findAll() {
    	List<Publication> listpub = new ArrayList<Publication>();
    	for (Publication pub : publicationRepository.findAll()) {
    		listpub.add(pub);
    	}
    	return listpub;
    }
    
    public void save(Publication publication) {
    	publicationRepository.save(publication);
    }
    
    public Publication findOne(Long id) {
    	return publicationRepository.findOne(id);
    }
    
    public void delete(Long id) {
    	publicationRepository.delete(id);
    }
    

}
