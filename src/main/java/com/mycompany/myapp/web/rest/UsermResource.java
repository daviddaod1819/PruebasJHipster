package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Userm;
import com.mycompany.myapp.service.UsermService;
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

    public UsermResource(UsermService usermService) {
        this.usermService = usermService;
    }

    /**
     * {@code POST  /userms} : Create a new userm.
     *
     * @param userm the userm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userm, or with status {@code 400 (Bad Request)} if the userm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/userms")
    public ResponseEntity<Userm> createUserm(@RequestBody Userm userm) throws URISyntaxException {
        log.debug("REST request to save Userm : {}", userm);
        if (userm.getId() != null) {
            throw new BadRequestAlertException("A new userm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Userm result = usermService.save(userm);
        return ResponseEntity
            .created(new URI("/api/userms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /userms} : Updates an existing userm.
     *
     * @param userm the userm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userm,
     * or with status {@code 400 (Bad Request)} if the userm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/userms")
    public ResponseEntity<Userm> updateUserm(@RequestBody Userm userm) throws URISyntaxException {
        log.debug("REST request to update Userm : {}", userm);
        if (userm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Userm result = usermService.save(userm);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userm.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /userms} : get all the userms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userms in body.
     */
    @GetMapping("/userms")
    public ResponseEntity<List<Userm>> getAllUserms(Pageable pageable) {
        log.debug("REST request to get a page of Userms");
        Page<Userm> page = usermService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /userms/:id} : get the "id" userm.
     *
     * @param id the id of the userm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/userms/{id}")
    public ResponseEntity<Userm> getUserm(@PathVariable Long id) {
        log.debug("REST request to get Userm : {}", id);
        Optional<Userm> userm = usermService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userm);
    }

    /**
     * {@code DELETE  /userms/:id} : delete the "id" userm.
     *
     * @param id the id of the userm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/userms/{id}")
    public ResponseEntity<Void> deleteUserm(@PathVariable Long id) {
        log.debug("REST request to delete Userm : {}", id);
        usermService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
