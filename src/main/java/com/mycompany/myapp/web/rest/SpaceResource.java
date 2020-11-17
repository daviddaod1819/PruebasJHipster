package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.service.SpaceQueryService;
import com.mycompany.myapp.service.SpaceService;
import com.mycompany.myapp.service.dto.SpaceCriteria;
import com.mycompany.myapp.service.dto.SpaceDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Space}.
 */
@RestController
@RequestMapping("/api")
public class SpaceResource {
    private final Logger log = LoggerFactory.getLogger(SpaceResource.class);

    private static final String ENTITY_NAME = "space";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpaceService spaceService;

    private final SpaceQueryService spaceQueryService;

    public SpaceResource(SpaceService spaceService, SpaceQueryService spaceQueryService) {
        this.spaceService = spaceService;
        this.spaceQueryService = spaceQueryService;
    }

    /**
     * {@code POST  /spaces} : Create a new space.
     *
     * @param spaceDTO the spaceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spaceDTO, or with status {@code 400 (Bad Request)} if the space has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spaces")
    public ResponseEntity<SpaceDTO> createSpace(@RequestBody SpaceDTO spaceDTO) throws URISyntaxException {
        log.debug("REST request to save Space : {}", spaceDTO);
        if (spaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new space cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpaceDTO result = spaceService.save(spaceDTO);
        return ResponseEntity
            .created(new URI("/api/spaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spaces} : Updates an existing space.
     *
     * @param spaceDTO the spaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spaceDTO,
     * or with status {@code 400 (Bad Request)} if the spaceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spaces")
    public ResponseEntity<SpaceDTO> updateSpace(@RequestBody SpaceDTO spaceDTO) throws URISyntaxException {
        log.debug("REST request to update Space : {}", spaceDTO);
        if (spaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpaceDTO result = spaceService.save(spaceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /spaces} : get all the spaces.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spaces in body.
     */
    @GetMapping("/spaces")
    public ResponseEntity<List<SpaceDTO>> getAllSpaces(SpaceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Spaces by criteria: {}", criteria);
        Page<SpaceDTO> page = spaceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spaces/count} : count all the spaces.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/spaces/count")
    public ResponseEntity<Long> countSpaces(SpaceCriteria criteria) {
        log.debug("REST request to count Spaces by criteria: {}", criteria);
        return ResponseEntity.ok().body(spaceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /spaces/:id} : get the "id" space.
     *
     * @param id the id of the spaceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spaceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spaces/{id}")
    public ResponseEntity<SpaceDTO> getSpace(@PathVariable Long id) {
        log.debug("REST request to get Space : {}", id);
        Optional<SpaceDTO> spaceDTO = spaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spaceDTO);
    }

    /**
     * {@code DELETE  /spaces/:id} : delete the "id" space.
     *
     * @param id the id of the spaceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spaces/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long id) {
        log.debug("REST request to delete Space : {}", id);
        spaceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/spaces?query=:query} : search for the space corresponding
     * to the query.
     *
     * @param query the query of the space search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/spaces")
    public ResponseEntity<List<SpaceDTO>> searchSpaces(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Spaces for query {}", query);
        Page<SpaceDTO> page = spaceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
