package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.UsermDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Userm}.
 */
public interface UsermService {
    /**
     * Save a userm.
     *
     * @param usermDTO the entity to save.
     * @return the persisted entity.
     */
    UsermDTO save(UsermDTO usermDTO);

    /**
     * Get all the userms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UsermDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UsermDTO> findOne(Long id);

    /**
     * Delete the "id" userm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userm corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UsermDTO> search(String query, Pageable pageable);
}
