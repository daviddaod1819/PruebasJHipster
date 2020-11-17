package com.mycompany.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.UserInfo;
import com.mycompany.myapp.repository.UserInfoRepository;
import com.mycompany.myapp.repository.search.UserInfoSearchRepository;
import com.mycompany.myapp.service.UserInfoService;
import com.mycompany.myapp.service.dto.UserInfoDTO;
import com.mycompany.myapp.service.mapper.UserInfoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserInfo}.
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
    private final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoRepository userInfoRepository;

    private final UserInfoMapper userInfoMapper;

    private final UserInfoSearchRepository userInfoSearchRepository;

    public UserInfoServiceImpl(
        UserInfoRepository userInfoRepository,
        UserInfoMapper userInfoMapper,
        UserInfoSearchRepository userInfoSearchRepository
    ) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoMapper = userInfoMapper;
        this.userInfoSearchRepository = userInfoSearchRepository;
    }

    @Override
    public UserInfoDTO save(UserInfoDTO userInfoDTO) {
        log.debug("Request to save UserInfo : {}", userInfoDTO);
        UserInfo userInfo = userInfoMapper.toEntity(userInfoDTO);
        userInfo = userInfoRepository.save(userInfo);
        UserInfoDTO result = userInfoMapper.toDto(userInfo);
        userInfoSearchRepository.save(userInfo);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserInfos");
        return userInfoRepository.findAll(pageable).map(userInfoMapper::toDto);
    }

    /**
     *  Get all the userInfos where User is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserInfoDTO> findAllWhereUserIsNull() {
        log.debug("Request to get all userInfos where User is null");
        return StreamSupport
            .stream(userInfoRepository.findAll().spliterator(), false)
            .filter(userInfo -> userInfo.getUser() == null)
            .map(userInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserInfoDTO> findOne(Long id) {
        log.debug("Request to get UserInfo : {}", id);
        return userInfoRepository.findById(id).map(userInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserInfo : {}", id);
        userInfoRepository.deleteById(id);
        userInfoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserInfos for query {}", query);
        return userInfoSearchRepository.search(queryStringQuery(query), pageable).map(userInfoMapper::toDto);
    }
}
