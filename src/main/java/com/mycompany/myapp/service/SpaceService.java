package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SpaceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Space}.
 */
public interface SpaceService {
    /**
     * Save a space.
     *
     * @param spaceDTO the entity to save.
     * @return the persisted entity.
     */
    SpaceDTO save(SpaceDTO spaceDTO);

    /**
     * Get all the spaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpaceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" space.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpaceDTO> findOne(Long id);

    /**
     * Delete the "id" space.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the space corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpaceDTO> search(String query, Pageable pageable);
}
