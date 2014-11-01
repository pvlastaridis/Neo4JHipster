package com.mycompany.myapp.web.rest;

import java.util.ArrayList;
import java.util.List;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Publication;
import com.mycompany.myapp.service.PublicationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;


/**
 * REST controller for managing Publication.
 */
@RestController
@RequestMapping("/app")
public class PublicationResource {

    private final Logger log = LoggerFactory.getLogger(PublicationResource.class);

    @Inject
    private PublicationService publicationService;

    /**
     * POST  /rest/publications -> Create a new publication.
     */
    @RequestMapping(value = "/rest/publications",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Publication publication) {
        log.debug("REST request to save Publication : {}", publication);
        publicationService.save(publication);
    }

    /**
     * GET  /rest/publications -> get all the publications.
     */
    @RequestMapping(value = "/rest/publications",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Publication> getAll() {
        log.debug("REST request to get all Publications");
        List<Publication> listpub = new ArrayList<Publication>();
    	for (Publication pub : publicationService.findAll()) {
    		listpub.add(pub);
    	}
    	return listpub;
	}

    /**
     * GET  /rest/publications/:id -> get the "id" publication.
     */
    @RequestMapping(value = "/rest/publications/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Publication> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Publication : {}", id);
        Publication publication = publicationService.findOne(id);
        if (publication == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(publication, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/publications/:id -> delete the "id" publication.
     */
    @RequestMapping(value = "/rest/publications/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Publication : {}", id);
        publicationService.delete(id);
    }
}
