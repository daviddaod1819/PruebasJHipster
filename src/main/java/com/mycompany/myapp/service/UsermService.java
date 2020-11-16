package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Userm;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Userm}.
 */
public interface UsermService {
    /**
     * Save a userm.
     *
     * @param userm the entity to save.
     * @return the persisted entity.
     */
    Userm save(Userm userm);

    /**
     * Get all the userms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Userm> findAll(Pageable pageable);

    /**
     * Get the "id" userm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Userm> findOne(Long id);

    /**
     * Delete the "id" userm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
