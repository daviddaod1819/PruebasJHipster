package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Space;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Space}.
 */
public interface SpaceService {
    /**
     * Save a space.
     *
     * @param space the entity to save.
     * @return the persisted entity.
     */
    Space save(Space space);

    /**
     * Get all the spaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Space> findAll(Pageable pageable);

    /**
     * Get the "id" space.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Space> findOne(Long id);

    /**
     * Delete the "id" space.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
