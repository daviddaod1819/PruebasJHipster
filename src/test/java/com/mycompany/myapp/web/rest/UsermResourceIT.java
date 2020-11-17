package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.PruebasJHipsterApp;
import com.mycompany.myapp.domain.UserInfo;
import com.mycompany.myapp.domain.Userm;
import com.mycompany.myapp.repository.UsermRepository;
import com.mycompany.myapp.repository.search.UsermSearchRepository;
import com.mycompany.myapp.service.UsermQueryService;
import com.mycompany.myapp.service.UsermService;
import com.mycompany.myapp.service.dto.UsermCriteria;
import com.mycompany.myapp.service.dto.UsermDTO;
import com.mycompany.myapp.service.mapper.UsermMapper;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsermResource} REST controller.
 */
@SpringBootTest(classes = PruebasJHipsterApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UsermResourceIT {
    @Autowired
    private UsermRepository usermRepository;

    @Autowired
    private UsermMapper usermMapper;

    @Autowired
    private UsermService usermService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.UsermSearchRepositoryMockConfiguration
     */
    @Autowired
    private UsermSearchRepository mockUsermSearchRepository;

    @Autowired
    private UsermQueryService usermQueryService;

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
        UsermDTO usermDTO = usermMapper.toDto(userm);
        restUsermMockMvc
            .perform(post("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usermDTO)))
            .andExpect(status().isCreated());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeCreate + 1);
        Userm testUserm = usermList.get(usermList.size() - 1);

        // Validate the Userm in Elasticsearch
        verify(mockUsermSearchRepository, times(1)).save(testUserm);
    }

    @Test
    @Transactional
    public void createUsermWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usermRepository.findAll().size();

        // Create the Userm with an existing ID
        userm.setId(1L);
        UsermDTO usermDTO = usermMapper.toDto(userm);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsermMockMvc
            .perform(post("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usermDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeCreate);

        // Validate the Userm in Elasticsearch
        verify(mockUsermSearchRepository, times(0)).save(userm);
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
    public void getUsermsByIdFiltering() throws Exception {
        // Initialize the database
        usermRepository.saveAndFlush(userm);

        Long id = userm.getId();

        defaultUsermShouldBeFound("id.equals=" + id);
        defaultUsermShouldNotBeFound("id.notEquals=" + id);

        defaultUsermShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsermShouldNotBeFound("id.greaterThan=" + id);

        defaultUsermShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsermShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllUsermsByUserInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        usermRepository.saveAndFlush(userm);
        UserInfo userInfo = UserInfoResourceIT.createEntity(em);
        em.persist(userInfo);
        em.flush();
        userm.setUserInfo(userInfo);
        usermRepository.saveAndFlush(userm);
        Long userInfoId = userInfo.getId();

        // Get all the usermList where userInfo equals to userInfoId
        defaultUsermShouldBeFound("userInfoId.equals=" + userInfoId);

        // Get all the usermList where userInfo equals to userInfoId + 1
        defaultUsermShouldNotBeFound("userInfoId.equals=" + (userInfoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsermShouldBeFound(String filter) throws Exception {
        restUsermMockMvc
            .perform(get("/api/userms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userm.getId().intValue())));

        // Check, that the count call also returns 1
        restUsermMockMvc
            .perform(get("/api/userms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsermShouldNotBeFound(String filter) throws Exception {
        restUsermMockMvc
            .perform(get("/api/userms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsermMockMvc
            .perform(get("/api/userms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        usermRepository.saveAndFlush(userm);

        int databaseSizeBeforeUpdate = usermRepository.findAll().size();

        // Update the userm
        Userm updatedUserm = usermRepository.findById(userm.getId()).get();
        // Disconnect from session so that the updates on updatedUserm are not directly saved in db
        em.detach(updatedUserm);
        UsermDTO usermDTO = usermMapper.toDto(updatedUserm);

        restUsermMockMvc
            .perform(put("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usermDTO)))
            .andExpect(status().isOk());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeUpdate);
        Userm testUserm = usermList.get(usermList.size() - 1);

        // Validate the Userm in Elasticsearch
        verify(mockUsermSearchRepository, times(1)).save(testUserm);
    }

    @Test
    @Transactional
    public void updateNonExistingUserm() throws Exception {
        int databaseSizeBeforeUpdate = usermRepository.findAll().size();

        // Create the Userm
        UsermDTO usermDTO = usermMapper.toDto(userm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsermMockMvc
            .perform(put("/api/userms").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usermDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Userm in the database
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Userm in Elasticsearch
        verify(mockUsermSearchRepository, times(0)).save(userm);
    }

    @Test
    @Transactional
    public void deleteUserm() throws Exception {
        // Initialize the database
        usermRepository.saveAndFlush(userm);

        int databaseSizeBeforeDelete = usermRepository.findAll().size();

        // Delete the userm
        restUsermMockMvc
            .perform(delete("/api/userms/{id}", userm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Userm> usermList = usermRepository.findAll();
        assertThat(usermList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Userm in Elasticsearch
        verify(mockUsermSearchRepository, times(1)).deleteById(userm.getId());
    }

    @Test
    @Transactional
    public void searchUserm() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        usermRepository.saveAndFlush(userm);
        when(mockUsermSearchRepository.search(queryStringQuery("id:" + userm.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userm), PageRequest.of(0, 1), 1));

        // Search the userm
        restUsermMockMvc
            .perform(get("/api/_search/userms?query=id:" + userm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userm.getId().intValue())));
    }
}
