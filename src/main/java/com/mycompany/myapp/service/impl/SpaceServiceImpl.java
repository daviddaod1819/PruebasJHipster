package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Space;
import com.mycompany.myapp.repository.SpaceRepository;
import com.mycompany.myapp.repository.search.SpaceSearchRepository;
import com.mycompany.myapp.service.SpaceService;
import com.mycompany.myapp.service.dto.SpaceDTO;
import com.mycompany.myapp.service.mapper.SpaceMapper;
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

    private final SpaceMapper spaceMapper;

    private final SpaceSearchRepository spaceSearchRepository;

    public SpaceServiceImpl(SpaceRepository spaceRepository, SpaceMapper spaceMapper, SpaceSearchRepository spaceSearchRepository) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
        this.spaceSearchRepository = spaceSearchRepository;
    }

    @Override
    public SpaceDTO save(SpaceDTO spaceDTO) {
        log.debug("Request to save Space : {}", spaceDTO);
        Space space = spaceMapper.toEntity(spaceDTO);
        space = spaceRepository.save(space);
        SpaceDTO result = spaceMapper.toDto(space);
        spaceSearchRepository.save(space);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Spaces");
        return spaceRepository.findAll(pageable).map(spaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpaceDTO> findOne(Long id) {
        log.debug("Request to get Space : {}", id);
        return spaceRepository.findById(id).map(spaceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Space : {}", id);
        spaceRepository.deleteById(id);
        spaceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpaceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Spaces for query {}", query);
        return spaceSearchRepository.search(queryStringQuery(query), pageable).map(spaceMapper::toDto);
    }
}
