package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Userm;
import com.mycompany.myapp.repository.UsermRepository;
import com.mycompany.myapp.service.UsermService;
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

    public UsermServiceImpl(UsermRepository usermRepository) {
        this.usermRepository = usermRepository;
    }

    @Override
    public Userm save(Userm userm) {
        log.debug("Request to save Userm : {}", userm);
        return usermRepository.save(userm);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Userm> findAll(Pageable pageable) {
        log.debug("Request to get all Userms");
        return usermRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Userm> findOne(Long id) {
        log.debug("Request to get Userm : {}", id);
        return usermRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Userm : {}", id);
        usermRepository.deleteById(id);
    }
}
