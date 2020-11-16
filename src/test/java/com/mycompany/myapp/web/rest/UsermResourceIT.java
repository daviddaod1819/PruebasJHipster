package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.PruebasJHipsterApp;
import com.mycompany.myapp.domain.Userm;
import com.mycompany.myapp.repository.UsermRepository;
import com.mycompany.myapp.service.UsermService;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsermResource} REST controller.
 */
@SpringBootTest(classes = PruebasJHipsterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UsermResourceIT {
    @Autowired
    private UsermRepository usermRepository;

    @Autowired
    private UsermService usermService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsermMockMvc;

    private Userm userm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Userm createEntity(EntityManager em) {
        Userm userm = new Userm();
        return userm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Userm createUpdatedEntity(EntityManager em) {
        Userm userm = new Userm();
        return userm;
    }

    @BeforeEach
    public void initTest() {
        userm = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserm() throws Exception {
        int databaseSizeBeforeCreate = usermRepository.findAll().size();
        // Create the Userm
        restUsermMockMvc
            .perform(post("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userm)))
            .andExpect(status().isCreated());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeCreate + 1);
        Userm testUserm = usermList.get(usermList.size() - 1);
    }

    @Test
    @Transactional
    public void createUsermWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usermRepository.findAll().size();

        // Create the Userm with an existing ID
        userm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsermMockMvc
            .perform(post("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userm)))
            .andExpect(status().isBadRequest());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserms() throws Exception {
        // Initialize the database
        usermRepository.saveAndFlush(userm);

        // Get all the usermList
        restUsermMockMvc
            .perform(get("/api/userms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userm.getId().intValue())));
    }

    @Test
    @Transactional
    public void getUserm() throws Exception {
        // Initialize the database
        usermRepository.saveAndFlush(userm);

        // Get the userm
        restUsermMockMvc
            .perform(get("/api/userms/{id}", userm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userm.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserm() throws Exception {
        // Get the userm
        restUsermMockMvc.perform(get("/api/userms/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserm() throws Exception {
        // Initialize the database
        usermService.save(userm);

        int databaseSizeBeforeUpdate = usermRepository.findAll().size();

        // Update the userm
        Userm updatedUserm = usermRepository.findById(userm.getId()).get();
        // Disconnect from session so that the updates on updatedUserm are not directly saved in db
        em.detach(updatedUserm);

        restUsermMockMvc
            .perform(put("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedUserm)))
            .andExpect(status().isOk());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeUpdate);
        Userm testUserm = usermList.get(usermList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingUserm() throws Exception {
        int databaseSizeBeforeUpdate = usermRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsermMockMvc
            .perform(put("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userm)))
            .andExpect(status().isBadRequest());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserm() throws Exception {
        // Initialize the database
        usermService.save(userm);

        int databaseSizeBeforeDelete = usermRepository.findAll().size();

        // Delete the userm
        restUsermMockMvc
            .perform(delete("/api/userms/{id}", userm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
