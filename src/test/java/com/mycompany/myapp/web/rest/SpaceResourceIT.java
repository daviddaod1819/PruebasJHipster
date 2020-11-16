package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.PruebasJHipsterApp;
import com.mycompany.myapp.domain.Space;
import com.mycompany.myapp.repository.SpaceRepository;
import com.mycompany.myapp.repository.search.SpaceSearchRepository;
import com.mycompany.myapp.service.SpaceService;
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
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@SpringBootTest(classes = PruebasJHipsterApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SpaceResourceIT {
    private static final Integer DEFAULT_ROOMS = 1;
    private static final Integer UPDATED_ROOMS = 2;

    private static final Integer DEFAULT_METERS = 1;
    private static final Integer UPDATED_METERS = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpaceService spaceService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.SpaceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SpaceSearchRepository mockSpaceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    private Space space;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Space createEntity(EntityManager em) {
        Space space = new Space().rooms(DEFAULT_ROOMS).meters(DEFAULT_METERS).price(DEFAULT_PRICE).details(DEFAULT_DETAILS);
        return space;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Space createUpdatedEntity(EntityManager em) {
        Space space = new Space().rooms(UPDATED_ROOMS).meters(UPDATED_METERS).price(UPDATED_PRICE).details(UPDATED_DETAILS);
        return space;
    }

    @BeforeEach
    public void initTest() {
        space = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpace() throws Exception {
        int databaseSizeBeforeCreate = spaceRepository.findAll().size();
        // Create the Space
        restSpaceMockMvc
            .perform(post("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(space)))
            .andExpect(status().isCreated());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeCreate + 1);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getRooms()).isEqualTo(DEFAULT_ROOMS);
        assertThat(testSpace.getMeters()).isEqualTo(DEFAULT_METERS);
        assertThat(testSpace.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSpace.getDetails()).isEqualTo(DEFAULT_DETAILS);

        // Validate the Space in Elasticsearch
        verify(mockSpaceSearchRepository, times(1)).save(testSpace);
    }

    @Test
    @Transactional
    public void createSpaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spaceRepository.findAll().size();

        // Create the Space with an existing ID
        space.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpaceMockMvc
            .perform(post("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(space)))
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Space in Elasticsearch
        verify(mockSpaceSearchRepository, times(0)).save(space);
    }

    @Test
    @Transactional
    public void getAllSpaces() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList
        restSpaceMockMvc
            .perform(get("/api/spaces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(space.getId().intValue())))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].meters").value(hasItem(DEFAULT_METERS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));
    }

    @Test
    @Transactional
    public void getSpace() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get the space
        restSpaceMockMvc
            .perform(get("/api/spaces/{id}", space.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(space.getId().intValue()))
            .andExpect(jsonPath("$.rooms").value(DEFAULT_ROOMS))
            .andExpect(jsonPath("$.meters").value(DEFAULT_METERS))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS));
    }

    @Test
    @Transactional
    public void getNonExistingSpace() throws Exception {
        // Get the space
        restSpaceMockMvc.perform(get("/api/spaces/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpace() throws Exception {
        // Initialize the database
        spaceService.save(space);

        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();

        // Update the space
        Space updatedSpace = spaceRepository.findById(space.getId()).get();
        // Disconnect from session so that the updates on updatedSpace are not directly saved in db
        em.detach(updatedSpace);
        updatedSpace.rooms(UPDATED_ROOMS).meters(UPDATED_METERS).price(UPDATED_PRICE).details(UPDATED_DETAILS);

        restSpaceMockMvc
            .perform(put("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedSpace)))
            .andExpect(status().isOk());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testSpace.getMeters()).isEqualTo(UPDATED_METERS);
        assertThat(testSpace.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSpace.getDetails()).isEqualTo(UPDATED_DETAILS);

        // Validate the Space in Elasticsearch
        verify(mockSpaceSearchRepository, times(2)).save(testSpace);
    }

    @Test
    @Transactional
    public void updateNonExistingSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(put("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(space)))
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Space in Elasticsearch
        verify(mockSpaceSearchRepository, times(0)).save(space);
    }

    @Test
    @Transactional
    public void deleteSpace() throws Exception {
        // Initialize the database
        spaceService.save(space);

        int databaseSizeBeforeDelete = spaceRepository.findAll().size();

        // Delete the space
        restSpaceMockMvc
            .perform(delete("/api/spaces/{id}", space.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Space in Elasticsearch
        verify(mockSpaceSearchRepository, times(1)).deleteById(space.getId());
    }

    @Test
    @Transactional
    public void searchSpace() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        spaceService.save(space);
        when(mockSpaceSearchRepository.search(queryStringQuery("id:" + space.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(space), PageRequest.of(0, 1), 1));

        // Search the space
        restSpaceMockMvc
            .perform(get("/api/_search/spaces?query=id:" + space.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(space.getId().intValue())))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].meters").value(hasItem(DEFAULT_METERS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));
    }
}
