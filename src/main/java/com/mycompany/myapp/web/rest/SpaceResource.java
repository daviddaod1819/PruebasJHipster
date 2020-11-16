package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Space;
import com.mycompany.myapp.service.SpaceService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
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

    public SpaceResource(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    /**
     * {@code POST  /spaces} : Create a new space.
     *
     * @param space the space to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new space, or with status {@code 400 (Bad Request)} if the space has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spaces")
    public ResponseEntity<Space> createSpace(@RequestBody Space space) throws URISyntaxException {
        log.debug("REST request to save Space : {}", space);
        if (space.getId() != null) {
            throw new BadRequestAlertException("A new space cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Space result = spaceService.save(space);
        return ResponseEntity
            .created(new URI("/api/spaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spaces} : Updates an existing space.
     *
     * @param space the space to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated space,
     * or with status {@code 400 (Bad Request)} if the space is not valid,
     * or with status {@code 500 (Internal Server Error)} if the space couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spaces")
    public ResponseEntity<Space> updateSpace(@RequestBody Space space) throws URISyntaxException {
        log.debug("REST request to update Space : {}", space);
        if (space.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Space result = spaceService.save(space);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, space.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /spaces} : get all the spaces.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spaces in body.
     */
    @GetMapping("/spaces")
    public ResponseEntity<List<Space>> getAllSpaces(Pageable pageable) {
        log.debug("REST request to get a page of Spaces");
        Page<Space> page = spaceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spaces/:id} : get the "id" space.
     *
     * @param id the id of the space to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the space, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spaces/{id}")
    public ResponseEntity<Space> getSpace(@PathVariable Long id) {
        log.debug("REST request to get Space : {}", id);
        Optional<Space> space = spaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(space);
    }

    /**
     * {@code DELETE  /spaces/:id} : delete the "id" space.
     *
     * @param id the id of the space to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spaces/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long id) {
        log.debug("REST request to delete Space : {}", id);
        spaceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
