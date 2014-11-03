package com.mycompany.myapp.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Author;
import com.mycompany.myapp.service.AuthorService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuthorResource REST controller.
 *
 * @see AuthorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AuthorResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    
    private static final int DEFAULT_NO_OF_BOOKS_PUBLISHED = 0;
    private static final int UPDATED_NO_OF_BOOKS_PUBLISHED = 1;
    
    private static final Long DEFAULT_WEIGHT = 0L;
    private static final Long UPDATED_WEIGHT = 1L;
    
    private static final BigDecimal DEFAULT_HEIGHT = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_HEIGHT = BigDecimal.ONE;
    
    private static final LocalDate DEFAULT_DATE_OF_BIRTH = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = new LocalDate();
    
   private static final Boolean DEFAULT_MARRIED = false;
   private static final Boolean UPDATED_MARRIED = true;

   @Inject
   private AuthorService authorService;

   private MockMvc restAuthorMockMvc;

   private Author author;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuthorResource authorResource = new AuthorResource();
        ReflectionTestUtils.setField(authorResource, "authorService", authorService);
        this.restAuthorMockMvc = MockMvcBuilders.standaloneSetup(authorResource).build();
    }

    @Before
    public void initTest() {
    	List<Author> authors  = authorService.findAll();
    	for (Author author : authors) {
    		authorService.delete(author.getId());
    	}
        author = new Author();
        author.setName(DEFAULT_NAME);
        author.setNoOfBooksPublished(DEFAULT_NO_OF_BOOKS_PUBLISHED);
        author.setWeight(DEFAULT_WEIGHT);
        author.setHeight(DEFAULT_HEIGHT);
        author.setDateOfBirth(DEFAULT_DATE_OF_BIRTH);
        author.setMarried(DEFAULT_MARRIED);
    }

    @Test
    public void createAuthor() throws Exception {
        // Validate the database is empty
        assertThat(authorService.findAll()).hasSize(0);

        // Create the Author
        restAuthorMockMvc.perform(post("/app/rest/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(author)))
                .andExpect(status().isOk());

        // Validate the Author in the database
        List<Author> authors = authorService.findAll();
        assertThat(authors).hasSize(1);
        Author testAuthor = authors.iterator().next();
        assertThat(testAuthor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuthor.getNoOfBooksPublished()).isEqualTo(DEFAULT_NO_OF_BOOKS_PUBLISHED);
        assertThat(testAuthor.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testAuthor.getHeight().doubleValue()).isEqualTo(DEFAULT_HEIGHT.doubleValue());
        assertThat(testAuthor.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testAuthor.getMarried()).isEqualTo(DEFAULT_MARRIED);;
    }

    @Test
    public void getAllAuthors() throws Exception {
        // Initialize the database
        authorService.save(author);

        // Get all the authors
        restAuthorMockMvc.perform(get("/app/rest/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(new Integer(author.getId().intValue())))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.[0].noOfBooksPublished").value(DEFAULT_NO_OF_BOOKS_PUBLISHED))
                .andExpect(jsonPath("$.[0].weight").value(DEFAULT_WEIGHT.intValue()))
                .andExpect(jsonPath("$.[0].height").value(DEFAULT_HEIGHT.doubleValue()))
                .andExpect(jsonPath("$.[0].dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
                .andExpect(jsonPath("$.[0].married").value(DEFAULT_MARRIED.booleanValue()));
    }

    @Test
    public void getAuthor() throws Exception {
        // Initialize the database
        authorService.save(author);

        // Get the author
        restAuthorMockMvc.perform(get("/app/rest/authors/{id}", author.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(author.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.noOfBooksPublished").value(DEFAULT_NO_OF_BOOKS_PUBLISHED))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.intValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.married").value(DEFAULT_MARRIED.booleanValue()));
    }

    @Test
    public void getNonExistingAuthor() throws Exception {
        // Get the author
        restAuthorMockMvc.perform(get("/app/rest/authors/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAuthor() throws Exception {
        // Initialize the database
        authorService.save(author);

        // Update the author
        author.setName(UPDATED_NAME);
        author.setNoOfBooksPublished(UPDATED_NO_OF_BOOKS_PUBLISHED);
        author.setWeight(UPDATED_WEIGHT);
        author.setHeight(UPDATED_HEIGHT);
        author.setDateOfBirth(UPDATED_DATE_OF_BIRTH);
        author.setMarried(UPDATED_MARRIED);
        restAuthorMockMvc.perform(post("/app/rest/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(author)))
                .andExpect(status().isOk());

        // Validate the Author in the database
        List<Author> authors = authorService.findAll();
        assertThat(authors).hasSize(1);
        Author testAuthor = authors.iterator().next();
        assertThat(testAuthor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuthor.getNoOfBooksPublished()).isEqualTo(UPDATED_NO_OF_BOOKS_PUBLISHED);
        assertThat(testAuthor.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testAuthor.getHeight().doubleValue()).isEqualTo(UPDATED_HEIGHT.doubleValue());
        assertThat(testAuthor.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testAuthor.getMarried()).isEqualTo(UPDATED_MARRIED);;
    }

    @Test
    public void deleteAuthor() throws Exception {
        // Initialize the database
        authorService.save(author);

        // Get the author
        restAuthorMockMvc.perform(delete("/app/rest/authors/{id}", author.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Author> authors = authorService.findAll();
        assertThat(authors).hasSize(0);
    }
}
