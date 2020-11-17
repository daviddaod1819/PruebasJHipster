package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.service.UsermQueryService;
import com.mycompany.myapp.service.UsermService;
import com.mycompany.myapp.service.dto.UsermCriteria;
import com.mycompany.myapp.service.dto.UsermDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Userm}.
 */
@RestController
@RequestMapping("/api")
public class UsermResource {
    private final Logger log = LoggerFactory.getLogger(UsermResource.class);

    private static final String ENTITY_NAME = "userm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsermService usermService;

    private final UsermQueryService usermQueryService;

    public UsermResource(UsermService usermService, UsermQueryService usermQueryService) {
        this.usermService = usermService;
        this.usermQueryService = usermQueryService;
    }

    /**
     * {@code POST  /userms} : Create a new userm.
     *
     * @param usermDTO the usermDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usermDTO, or with status {@code 400 (Bad Request)} if the userm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/userms")
    public ResponseEntity<UsermDTO> createUserm(@RequestBody UsermDTO usermDTO) throws URISyntaxException {
        log.debug("REST request to save Userm : {}", usermDTO);
        if (usermDTO.getId() != null) {
            throw new BadRequestAlertException("A new userm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UsermDTO result = usermService.save(usermDTO);
        return ResponseEntity
            .created(new URI("/api/userms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /userms} : Updates an existing userm.
     *
     * @param usermDTO the usermDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usermDTO,
     * or with status {@code 400 (Bad Request)} if the usermDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usermDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/userms")
    public ResponseEntity<UsermDTO> updateUserm(@RequestBody UsermDTO usermDTO) throws URISyntaxException {
        log.debug("REST request to update Userm : {}", usermDTO);
        if (usermDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UsermDTO result = usermService.save(usermDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, usermDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /userms} : get all the userms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userms in body.
     */
    @GetMapping("/userms")
    public ResponseEntity<List<UsermDTO>> getAllUserms(UsermCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Userms by criteria: {}", criteria);
        Page<UsermDTO> page = usermQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /userms/count} : count all the userms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/userms/count")
    public ResponseEntity<Long> countUserms(UsermCriteria criteria) {
        log.debug("REST request to count Userms by criteria: {}", criteria);
        return ResponseEntity.ok().body(usermQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /userms/:id} : get the "id" userm.
     *
     * @param id the id of the usermDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usermDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/userms/{id}")
    public ResponseEntity<UsermDTO> getUserm(@PathVariable Long id) {
        log.debug("REST request to get Userm : {}", id);
        Optional<UsermDTO> usermDTO = usermService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usermDTO);
    }

    /**
     * {@code DELETE  /userms/:id} : delete the "id" userm.
     *
     * @param id the id of the usermDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/userms/{id}")
    public ResponseEntity<Void> deleteUserm(@PathVariable Long id) {
        log.debug("REST request to delete Userm : {}", id);
        usermService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/userms?query=:query} : search for the userm corresponding
     * to the query.
     *
     * @param query the query of the userm search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/userms")
    public ResponseEntity<List<UsermDTO>> searchUserms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Userms for query {}", query);
        Page<UsermDTO> page = usermService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
