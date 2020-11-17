package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.PruebasJHipsterApp;
import com.mycompany.myapp.domain.Space;
import com.mycompany.myapp.domain.UserInfo;
import com.mycompany.myapp.repository.UserInfoRepository;
import com.mycompany.myapp.repository.search.UserInfoSearchRepository;
import com.mycompany.myapp.service.UserInfoQueryService;
import com.mycompany.myapp.service.UserInfoService;
import com.mycompany.myapp.service.dto.UserInfoCriteria;
import com.mycompany.myapp.service.dto.UserInfoDTO;
import com.mycompany.myapp.service.mapper.UserInfoMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserInfoResource} REST controller.
 */
@SpringBootTest(classes = PruebasJHipsterApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserInfoResourceIT {
    private static final Boolean DEFAULT_SEX = false;
    private static final Boolean UPDATED_SEX = true;

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_TOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOWN = "BBBBBBBBBB";

    private static final String DEFAULT_POST_CODE = "AAAAA";
    private static final String UPDATED_POST_CODE = "BBBBB";

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.UserInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserInfoSearchRepository mockUserInfoSearchRepository;

    @Autowired
    private UserInfoQueryService userInfoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .sex(DEFAULT_SEX)
            .birthDate(DEFAULT_BIRTH_DATE)
            .country(DEFAULT_COUNTRY)
            .town(DEFAULT_TOWN)
            .postCode(DEFAULT_POST_CODE);
        return userInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createUpdatedEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .sex(UPDATED_SEX)
            .birthDate(UPDATED_BIRTH_DATE)
            .country(UPDATED_COUNTRY)
            .town(UPDATED_TOWN)
            .postCode(UPDATED_POST_CODE);
        return userInfo;
    }

    @BeforeEach
    public void initTest() {
        userInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();
        // Create the UserInfo
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(userInfo);
        restUserInfoMockMvc
            .perform(
                post("/api/user-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.isSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testUserInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testUserInfo.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserInfo.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testUserInfo.getPostCode()).isEqualTo(DEFAULT_POST_CODE);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(1)).save(testUserInfo);
    }

    @Test
    @Transactional
    public void createUserInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo with an existing ID
        userInfo.setId(1L);
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(userInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInfoMockMvc
            .perform(
                post("/api/user-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(0)).save(userInfo);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userInfoRepository.findAll().size();
        // set the field null
        userInfo.setBirthDate(null);

        // Create the UserInfo, which fails.
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(userInfo);

        restUserInfoMockMvc
            .perform(
                post("/api/user-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList
        restUserInfoMockMvc
            .perform(get("/api/user-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.booleanValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)));
    }

    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc
            .perform(get("/api/user-infos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.booleanValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE));
    }

    @Test
    @Transactional
    public void getUserInfosByIdFiltering() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        Long id = userInfo.getId();

        defaultUserInfoShouldBeFound("id.equals=" + id);
        defaultUserInfoShouldNotBeFound("id.notEquals=" + id);

        defaultUserInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultUserInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserInfoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllUserInfosBySexIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where sex equals to DEFAULT_SEX
        defaultUserInfoShouldBeFound("sex.equals=" + DEFAULT_SEX);

        // Get all the userInfoList where sex equals to UPDATED_SEX
        defaultUserInfoShouldNotBeFound("sex.equals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    public void getAllUserInfosBySexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where sex not equals to DEFAULT_SEX
        defaultUserInfoShouldNotBeFound("sex.notEquals=" + DEFAULT_SEX);

        // Get all the userInfoList where sex not equals to UPDATED_SEX
        defaultUserInfoShouldBeFound("sex.notEquals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    public void getAllUserInfosBySexIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where sex in DEFAULT_SEX or UPDATED_SEX
        defaultUserInfoShouldBeFound("sex.in=" + DEFAULT_SEX + "," + UPDATED_SEX);

        // Get all the userInfoList where sex equals to UPDATED_SEX
        defaultUserInfoShouldNotBeFound("sex.in=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    public void getAllUserInfosBySexIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where sex is not null
        defaultUserInfoShouldBeFound("sex.specified=true");

        // Get all the userInfoList where sex is null
        defaultUserInfoShouldNotBeFound("sex.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultUserInfoShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the userInfoList where birthDate equals to UPDATED_BIRTH_DATE
        defaultUserInfoShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultUserInfoShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the userInfoList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultUserInfoShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultUserInfoShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the userInfoList where birthDate equals to UPDATED_BIRTH_DATE
        defaultUserInfoShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where birthDate is not null
        defaultUserInfoShouldBeFound("birthDate.specified=true");

        // Get all the userInfoList where birthDate is null
        defaultUserInfoShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where country equals to DEFAULT_COUNTRY
        defaultUserInfoShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the userInfoList where country equals to UPDATED_COUNTRY
        defaultUserInfoShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserInfosByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where country not equals to DEFAULT_COUNTRY
        defaultUserInfoShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the userInfoList where country not equals to UPDATED_COUNTRY
        defaultUserInfoShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserInfosByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultUserInfoShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the userInfoList where country equals to UPDATED_COUNTRY
        defaultUserInfoShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserInfosByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where country is not null
        defaultUserInfoShouldBeFound("country.specified=true");

        // Get all the userInfoList where country is null
        defaultUserInfoShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByCountryContainsSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where country contains DEFAULT_COUNTRY
        defaultUserInfoShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the userInfoList where country contains UPDATED_COUNTRY
        defaultUserInfoShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserInfosByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where country does not contain DEFAULT_COUNTRY
        defaultUserInfoShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the userInfoList where country does not contain UPDATED_COUNTRY
        defaultUserInfoShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserInfosByTownIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where town equals to DEFAULT_TOWN
        defaultUserInfoShouldBeFound("town.equals=" + DEFAULT_TOWN);

        // Get all the userInfoList where town equals to UPDATED_TOWN
        defaultUserInfoShouldNotBeFound("town.equals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByTownIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where town not equals to DEFAULT_TOWN
        defaultUserInfoShouldNotBeFound("town.notEquals=" + DEFAULT_TOWN);

        // Get all the userInfoList where town not equals to UPDATED_TOWN
        defaultUserInfoShouldBeFound("town.notEquals=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByTownIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where town in DEFAULT_TOWN or UPDATED_TOWN
        defaultUserInfoShouldBeFound("town.in=" + DEFAULT_TOWN + "," + UPDATED_TOWN);

        // Get all the userInfoList where town equals to UPDATED_TOWN
        defaultUserInfoShouldNotBeFound("town.in=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByTownIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where town is not null
        defaultUserInfoShouldBeFound("town.specified=true");

        // Get all the userInfoList where town is null
        defaultUserInfoShouldNotBeFound("town.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByTownContainsSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where town contains DEFAULT_TOWN
        defaultUserInfoShouldBeFound("town.contains=" + DEFAULT_TOWN);

        // Get all the userInfoList where town contains UPDATED_TOWN
        defaultUserInfoShouldNotBeFound("town.contains=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByTownNotContainsSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where town does not contain DEFAULT_TOWN
        defaultUserInfoShouldNotBeFound("town.doesNotContain=" + DEFAULT_TOWN);

        // Get all the userInfoList where town does not contain UPDATED_TOWN
        defaultUserInfoShouldBeFound("town.doesNotContain=" + UPDATED_TOWN);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPostCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where postCode equals to DEFAULT_POST_CODE
        defaultUserInfoShouldBeFound("postCode.equals=" + DEFAULT_POST_CODE);

        // Get all the userInfoList where postCode equals to UPDATED_POST_CODE
        defaultUserInfoShouldNotBeFound("postCode.equals=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPostCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where postCode not equals to DEFAULT_POST_CODE
        defaultUserInfoShouldNotBeFound("postCode.notEquals=" + DEFAULT_POST_CODE);

        // Get all the userInfoList where postCode not equals to UPDATED_POST_CODE
        defaultUserInfoShouldBeFound("postCode.notEquals=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPostCodeIsInShouldWork() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where postCode in DEFAULT_POST_CODE or UPDATED_POST_CODE
        defaultUserInfoShouldBeFound("postCode.in=" + DEFAULT_POST_CODE + "," + UPDATED_POST_CODE);

        // Get all the userInfoList where postCode equals to UPDATED_POST_CODE
        defaultUserInfoShouldNotBeFound("postCode.in=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPostCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where postCode is not null
        defaultUserInfoShouldBeFound("postCode.specified=true");

        // Get all the userInfoList where postCode is null
        defaultUserInfoShouldNotBeFound("postCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserInfosByPostCodeContainsSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where postCode contains DEFAULT_POST_CODE
        defaultUserInfoShouldBeFound("postCode.contains=" + DEFAULT_POST_CODE);

        // Get all the userInfoList where postCode contains UPDATED_POST_CODE
        defaultUserInfoShouldNotBeFound("postCode.contains=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllUserInfosByPostCodeNotContainsSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList where postCode does not contain DEFAULT_POST_CODE
        defaultUserInfoShouldNotBeFound("postCode.doesNotContain=" + DEFAULT_POST_CODE);

        // Get all the userInfoList where postCode does not contain UPDATED_POST_CODE
        defaultUserInfoShouldBeFound("postCode.doesNotContain=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void getAllUserInfosBySpacesIsEqualToSomething() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);
        Space spaces = SpaceResourceIT.createEntity(em);
        em.persist(spaces);
        em.flush();
        userInfo.addSpaces(spaces);
        userInfoRepository.saveAndFlush(userInfo);
        Long spacesId = spaces.getId();

        // Get all the userInfoList where spaces equals to spacesId
        defaultUserInfoShouldBeFound("spacesId.equals=" + spacesId);

        // Get all the userInfoList where spaces equals to spacesId + 1
        defaultUserInfoShouldNotBeFound("spacesId.equals=" + (spacesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserInfoShouldBeFound(String filter) throws Exception {
        restUserInfoMockMvc
            .perform(get("/api/user-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.booleanValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)));

        // Check, that the count call also returns 1
        restUserInfoMockMvc
            .perform(get("/api/user-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserInfoShouldNotBeFound(String filter) throws Exception {
        restUserInfoMockMvc
            .perform(get("/api/user-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserInfoMockMvc
            .perform(get("/api/user-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on updatedUserInfo are not directly saved in db
        em.detach(updatedUserInfo);
        updatedUserInfo
            .sex(UPDATED_SEX)
            .birthDate(UPDATED_BIRTH_DATE)
            .country(UPDATED_COUNTRY)
            .town(UPDATED_TOWN)
            .postCode(UPDATED_POST_CODE);
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(updatedUserInfo);

        restUserInfoMockMvc
            .perform(put("/api/user-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.isSex()).isEqualTo(UPDATED_SEX);
        assertThat(testUserInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUserInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserInfo.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testUserInfo.getPostCode()).isEqualTo(UPDATED_POST_CODE);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(1)).save(testUserInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Create the UserInfo
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(userInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInfoMockMvc
            .perform(put("/api/user-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(0)).save(userInfo);
    }

    @Test
    @Transactional
    public void deleteUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Delete the userInfo
        restUserInfoMockMvc
            .perform(delete("/api/user-infos/{id}", userInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserInfo in Elasticsearch
        verify(mockUserInfoSearchRepository, times(1)).deleteById(userInfo.getId());
    }

    @Test
    @Transactional
    public void searchUserInfo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);
        when(mockUserInfoSearchRepository.search(queryStringQuery("id:" + userInfo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userInfo), PageRequest.of(0, 1), 1));

        // Search the userInfo
        restUserInfoMockMvc
            .perform(get("/api/_search/user-infos?query=id:" + userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.booleanValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)));
    }
}
