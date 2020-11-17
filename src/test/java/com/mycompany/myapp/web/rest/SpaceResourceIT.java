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
import com.mycompany.myapp.service.SpaceQueryService;
import com.mycompany.myapp.service.SpaceService;
import com.mycompany.myapp.service.dto.SpaceCriteria;
import com.mycompany.myapp.service.dto.SpaceDTO;
import com.mycompany.myapp.service.mapper.SpaceMapper;
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
    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROOMS = 1;
    private static final Integer UPDATED_ROOMS = 2;
    private static final Integer SMALLER_ROOMS = 1 - 1;

    private static final Integer DEFAULT_METERS = 1;
    private static final Integer UPDATED_METERS = 2;
    private static final Integer SMALLER_METERS = 1 - 1;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;
    private static final Integer SMALLER_PRICE = 1 - 1;

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpaceMapper spaceMapper;

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
    private SpaceQueryService spaceQueryService;

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
        Space space = new Space()
            .title(DEFAULT_TITLE)
            .rooms(DEFAULT_ROOMS)
            .meters(DEFAULT_METERS)
            .price(DEFAULT_PRICE)
            .details(DEFAULT_DETAILS);
        return space;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Space createUpdatedEntity(EntityManager em) {
        Space space = new Space()
            .title(UPDATED_TITLE)
            .rooms(UPDATED_ROOMS)
            .meters(UPDATED_METERS)
            .price(UPDATED_PRICE)
            .details(UPDATED_DETAILS);
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
        SpaceDTO spaceDTO = spaceMapper.toDto(space);
        restSpaceMockMvc
            .perform(post("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isCreated());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeCreate + 1);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getTitle()).isEqualTo(DEFAULT_TITLE);
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
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpaceMockMvc
            .perform(post("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
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
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
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
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.rooms").value(DEFAULT_ROOMS))
            .andExpect(jsonPath("$.meters").value(DEFAULT_METERS))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS));
    }

    @Test
    @Transactional
    public void getSpacesByIdFiltering() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        Long id = space.getId();

        defaultSpaceShouldBeFound("id.equals=" + id);
        defaultSpaceShouldNotBeFound("id.notEquals=" + id);

        defaultSpaceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSpaceShouldNotBeFound("id.greaterThan=" + id);

        defaultSpaceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSpaceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllSpacesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where title equals to DEFAULT_TITLE
        defaultSpaceShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the spaceList where title equals to UPDATED_TITLE
        defaultSpaceShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSpacesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where title not equals to DEFAULT_TITLE
        defaultSpaceShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the spaceList where title not equals to UPDATED_TITLE
        defaultSpaceShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSpacesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSpaceShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the spaceList where title equals to UPDATED_TITLE
        defaultSpaceShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSpacesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where title is not null
        defaultSpaceShouldBeFound("title.specified=true");

        // Get all the spaceList where title is null
        defaultSpaceShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllSpacesByTitleContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where title contains DEFAULT_TITLE
        defaultSpaceShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the spaceList where title contains UPDATED_TITLE
        defaultSpaceShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSpacesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where title does not contain DEFAULT_TITLE
        defaultSpaceShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the spaceList where title does not contain UPDATED_TITLE
        defaultSpaceShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms equals to DEFAULT_ROOMS
        defaultSpaceShouldBeFound("rooms.equals=" + DEFAULT_ROOMS);

        // Get all the spaceList where rooms equals to UPDATED_ROOMS
        defaultSpaceShouldNotBeFound("rooms.equals=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms not equals to DEFAULT_ROOMS
        defaultSpaceShouldNotBeFound("rooms.notEquals=" + DEFAULT_ROOMS);

        // Get all the spaceList where rooms not equals to UPDATED_ROOMS
        defaultSpaceShouldBeFound("rooms.notEquals=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms in DEFAULT_ROOMS or UPDATED_ROOMS
        defaultSpaceShouldBeFound("rooms.in=" + DEFAULT_ROOMS + "," + UPDATED_ROOMS);

        // Get all the spaceList where rooms equals to UPDATED_ROOMS
        defaultSpaceShouldNotBeFound("rooms.in=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms is not null
        defaultSpaceShouldBeFound("rooms.specified=true");

        // Get all the spaceList where rooms is null
        defaultSpaceShouldNotBeFound("rooms.specified=false");
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms is greater than or equal to DEFAULT_ROOMS
        defaultSpaceShouldBeFound("rooms.greaterThanOrEqual=" + DEFAULT_ROOMS);

        // Get all the spaceList where rooms is greater than or equal to UPDATED_ROOMS
        defaultSpaceShouldNotBeFound("rooms.greaterThanOrEqual=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms is less than or equal to DEFAULT_ROOMS
        defaultSpaceShouldBeFound("rooms.lessThanOrEqual=" + DEFAULT_ROOMS);

        // Get all the spaceList where rooms is less than or equal to SMALLER_ROOMS
        defaultSpaceShouldNotBeFound("rooms.lessThanOrEqual=" + SMALLER_ROOMS);
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsLessThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms is less than DEFAULT_ROOMS
        defaultSpaceShouldNotBeFound("rooms.lessThan=" + DEFAULT_ROOMS);

        // Get all the spaceList where rooms is less than UPDATED_ROOMS
        defaultSpaceShouldBeFound("rooms.lessThan=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    public void getAllSpacesByRoomsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where rooms is greater than DEFAULT_ROOMS
        defaultSpaceShouldNotBeFound("rooms.greaterThan=" + DEFAULT_ROOMS);

        // Get all the spaceList where rooms is greater than SMALLER_ROOMS
        defaultSpaceShouldBeFound("rooms.greaterThan=" + SMALLER_ROOMS);
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters equals to DEFAULT_METERS
        defaultSpaceShouldBeFound("meters.equals=" + DEFAULT_METERS);

        // Get all the spaceList where meters equals to UPDATED_METERS
        defaultSpaceShouldNotBeFound("meters.equals=" + UPDATED_METERS);
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters not equals to DEFAULT_METERS
        defaultSpaceShouldNotBeFound("meters.notEquals=" + DEFAULT_METERS);

        // Get all the spaceList where meters not equals to UPDATED_METERS
        defaultSpaceShouldBeFound("meters.notEquals=" + UPDATED_METERS);
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters in DEFAULT_METERS or UPDATED_METERS
        defaultSpaceShouldBeFound("meters.in=" + DEFAULT_METERS + "," + UPDATED_METERS);

        // Get all the spaceList where meters equals to UPDATED_METERS
        defaultSpaceShouldNotBeFound("meters.in=" + UPDATED_METERS);
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters is not null
        defaultSpaceShouldBeFound("meters.specified=true");

        // Get all the spaceList where meters is null
        defaultSpaceShouldNotBeFound("meters.specified=false");
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters is greater than or equal to DEFAULT_METERS
        defaultSpaceShouldBeFound("meters.greaterThanOrEqual=" + DEFAULT_METERS);

        // Get all the spaceList where meters is greater than or equal to UPDATED_METERS
        defaultSpaceShouldNotBeFound("meters.greaterThanOrEqual=" + UPDATED_METERS);
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters is less than or equal to DEFAULT_METERS
        defaultSpaceShouldBeFound("meters.lessThanOrEqual=" + DEFAULT_METERS);

        // Get all the spaceList where meters is less than or equal to SMALLER_METERS
        defaultSpaceShouldNotBeFound("meters.lessThanOrEqual=" + SMALLER_METERS);
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsLessThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters is less than DEFAULT_METERS
        defaultSpaceShouldNotBeFound("meters.lessThan=" + DEFAULT_METERS);

        // Get all the spaceList where meters is less than UPDATED_METERS
        defaultSpaceShouldBeFound("meters.lessThan=" + UPDATED_METERS);
    }

    @Test
    @Transactional
    public void getAllSpacesByMetersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where meters is greater than DEFAULT_METERS
        defaultSpaceShouldNotBeFound("meters.greaterThan=" + DEFAULT_METERS);

        // Get all the spaceList where meters is greater than SMALLER_METERS
        defaultSpaceShouldBeFound("meters.greaterThan=" + SMALLER_METERS);
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price equals to DEFAULT_PRICE
        defaultSpaceShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the spaceList where price equals to UPDATED_PRICE
        defaultSpaceShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price not equals to DEFAULT_PRICE
        defaultSpaceShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the spaceList where price not equals to UPDATED_PRICE
        defaultSpaceShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultSpaceShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the spaceList where price equals to UPDATED_PRICE
        defaultSpaceShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price is not null
        defaultSpaceShouldBeFound("price.specified=true");

        // Get all the spaceList where price is null
        defaultSpaceShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price is greater than or equal to DEFAULT_PRICE
        defaultSpaceShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the spaceList where price is greater than or equal to UPDATED_PRICE
        defaultSpaceShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price is less than or equal to DEFAULT_PRICE
        defaultSpaceShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the spaceList where price is less than or equal to SMALLER_PRICE
        defaultSpaceShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price is less than DEFAULT_PRICE
        defaultSpaceShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the spaceList where price is less than UPDATED_PRICE
        defaultSpaceShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllSpacesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where price is greater than DEFAULT_PRICE
        defaultSpaceShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the spaceList where price is greater than SMALLER_PRICE
        defaultSpaceShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllSpacesByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where details equals to DEFAULT_DETAILS
        defaultSpaceShouldBeFound("details.equals=" + DEFAULT_DETAILS);

        // Get all the spaceList where details equals to UPDATED_DETAILS
        defaultSpaceShouldNotBeFound("details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void getAllSpacesByDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where details not equals to DEFAULT_DETAILS
        defaultSpaceShouldNotBeFound("details.notEquals=" + DEFAULT_DETAILS);

        // Get all the spaceList where details not equals to UPDATED_DETAILS
        defaultSpaceShouldBeFound("details.notEquals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void getAllSpacesByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where details in DEFAULT_DETAILS or UPDATED_DETAILS
        defaultSpaceShouldBeFound("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS);

        // Get all the spaceList where details equals to UPDATED_DETAILS
        defaultSpaceShouldNotBeFound("details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void getAllSpacesByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where details is not null
        defaultSpaceShouldBeFound("details.specified=true");

        // Get all the spaceList where details is null
        defaultSpaceShouldNotBeFound("details.specified=false");
    }

    @Test
    @Transactional
    public void getAllSpacesByDetailsContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where details contains DEFAULT_DETAILS
        defaultSpaceShouldBeFound("details.contains=" + DEFAULT_DETAILS);

        // Get all the spaceList where details contains UPDATED_DETAILS
        defaultSpaceShouldNotBeFound("details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void getAllSpacesByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList where details does not contain DEFAULT_DETAILS
        defaultSpaceShouldNotBeFound("details.doesNotContain=" + DEFAULT_DETAILS);

        // Get all the spaceList where details does not contain UPDATED_DETAILS
        defaultSpaceShouldBeFound("details.doesNotContain=" + UPDATED_DETAILS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpaceShouldBeFound(String filter) throws Exception {
        restSpaceMockMvc
            .perform(get("/api/spaces?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(space.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].meters").value(hasItem(DEFAULT_METERS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));

        // Check, that the count call also returns 1
        restSpaceMockMvc
            .perform(get("/api/spaces/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpaceShouldNotBeFound(String filter) throws Exception {
        restSpaceMockMvc
            .perform(get("/api/spaces?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpaceMockMvc
            .perform(get("/api/spaces/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        spaceRepository.saveAndFlush(space);

        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();

        // Update the space
        Space updatedSpace = spaceRepository.findById(space.getId()).get();
        // Disconnect from session so that the updates on updatedSpace are not directly saved in db
        em.detach(updatedSpace);
        updatedSpace.title(UPDATED_TITLE).rooms(UPDATED_ROOMS).meters(UPDATED_METERS).price(UPDATED_PRICE).details(UPDATED_DETAILS);
        SpaceDTO spaceDTO = spaceMapper.toDto(updatedSpace);

        restSpaceMockMvc
            .perform(put("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
            .andExpect(status().isOk());

        // Validate the Space in the database
        List<Space> spaceList = spaceRepository.findAll();
        assertThat(spaceList).hasSize(databaseSizeBeforeUpdate);
        Space testSpace = spaceList.get(spaceList.size() - 1);
        assertThat(testSpace.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpace.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testSpace.getMeters()).isEqualTo(UPDATED_METERS);
        assertThat(testSpace.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSpace.getDetails()).isEqualTo(UPDATED_DETAILS);

        // Validate the Space in Elasticsearch
        verify(mockSpaceSearchRepository, times(1)).save(testSpace);
    }

    @Test
    @Transactional
    public void updateNonExistingSpace() throws Exception {
        int databaseSizeBeforeUpdate = spaceRepository.findAll().size();

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(put("/api/spaces").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaceDTO)))
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
        spaceRepository.saveAndFlush(space);

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
        spaceRepository.saveAndFlush(space);
        when(mockSpaceSearchRepository.search(queryStringQuery("id:" + space.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(space), PageRequest.of(0, 1), 1));

        // Search the space
        restSpaceMockMvc
            .perform(get("/api/_search/spaces?query=id:" + space.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(space.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].meters").value(hasItem(DEFAULT_METERS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));
    }
}
