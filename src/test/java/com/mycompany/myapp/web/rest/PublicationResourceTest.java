package com.mycompany.myapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Publication;
import com.mycompany.myapp.repository.PublicationRepository;

/**
 * Test class for the PublicationResource REST controller.
 *
 * @see PublicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
public class PublicationResourceTest {
    
    private static final String DEFAULT_ID = "1";
    
    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
        
    @Inject
    private PublicationRepository publicationRepository;

    private MockMvc restPublicationMockMvc;

    private Publication publication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PublicationResource publicationResource = new PublicationResource();
        ReflectionTestUtils.setField(publicationResource, "publicationRepository", publicationRepository);

        this.restPublicationMockMvc = MockMvcBuilders.standaloneSetup(publicationResource).build();

        publication = new Publication();
        //publication.setId(DEFAULT_ID);

        publication.setTitle(DEFAULT_TITLE);
    }

    @Test
    public void testCRUDPublication() throws Exception {

        // Create Publication
        restPublicationMockMvc.perform(post("/app/rest/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publication)))
                .andExpect(status().isOk());

        // Read Publication
        restPublicationMockMvc.perform(get("/app/rest/publications/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));

        // Update Publication
        publication.setTitle(UPDATED_TITLE);

        restPublicationMockMvc.perform(post("/app/rest/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publication)))
                .andExpect(status().isOk());

        // Read updated Publication
        restPublicationMockMvc.perform(get("/app/rest/publications/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.title").value(UPDATED_TITLE.toString()));

        // Delete Publication
        restPublicationMockMvc.perform(delete("/app/rest/publications/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Read nonexisting Publication
        restPublicationMockMvc.perform(get("/app/rest/publications/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
