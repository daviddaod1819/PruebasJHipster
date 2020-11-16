package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Space;
import com.mycompany.myapp.repository.SpaceRepository;
import com.mycompany.myapp.service.SpaceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Space}.
 */
@Service
@Transactional
public class SpaceServiceImpl implements SpaceService {
    private final Logger log = LoggerFactory.getLogger(SpaceServiceImpl.class);

    private final SpaceRepository spaceRepository;

    public SpaceServiceImpl(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @Override
    public Space save(Space space) {
        log.debug("Request to save Space : {}", space);
        return spaceRepository.save(space);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Space> findAll(Pageable pageable) {
        log.debug("Request to get all Spaces");
        return spaceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Space> findOne(Long id) {
        log.debug("Request to get Space : {}", id);
        return spaceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Space : {}", id);
        spaceRepository.deleteById(id);
    }
}
