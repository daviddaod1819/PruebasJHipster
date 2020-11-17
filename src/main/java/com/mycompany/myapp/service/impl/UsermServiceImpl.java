package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Userm;
import com.mycompany.myapp.repository.UsermRepository;
import com.mycompany.myapp.repository.search.UsermSearchRepository;
import com.mycompany.myapp.service.UsermService;
import com.mycompany.myapp.service.dto.UsermDTO;
import com.mycompany.myapp.service.mapper.UsermMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Userm}.
 */
@Service
@Transactional
public class UsermServiceImpl implements UsermService {
    private final Logger log = LoggerFactory.getLogger(UsermServiceImpl.class);

    private final UsermRepository usermRepository;

    private final UsermMapper usermMapper;

    private final UsermSearchRepository usermSearchRepository;

    public UsermServiceImpl(UsermRepository usermRepository, UsermMapper usermMapper, UsermSearchRepository usermSearchRepository) {
        this.usermRepository = usermRepository;
        this.usermMapper = usermMapper;
        this.usermSearchRepository = usermSearchRepository;
    }

    @Override
    public UsermDTO save(UsermDTO usermDTO) {
        log.debug("Request to save Userm : {}", usermDTO);
        Userm userm = usermMapper.toEntity(usermDTO);
        userm = usermRepository.save(userm);
        UsermDTO result = usermMapper.toDto(userm);
        usermSearchRepository.save(userm);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsermDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Userms");
        return usermRepository.findAll(pageable).map(usermMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsermDTO> findOne(Long id) {
        log.debug("Request to get Userm : {}", id);
        return usermRepository.findById(id).map(usermMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Userm : {}", id);
        usermRepository.deleteById(id);
        usermSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsermDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Userms for query {}", query);
        return usermSearchRepository.search(queryStringQuery(query), pageable).map(usermMapper::toDto);
    }
}
