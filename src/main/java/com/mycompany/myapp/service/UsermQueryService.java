package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Userm;
import com.mycompany.myapp.repository.UsermRepository;
import com.mycompany.myapp.repository.search.UsermSearchRepository;
import com.mycompany.myapp.service.dto.UsermCriteria;
import com.mycompany.myapp.service.dto.UsermDTO;
import com.mycompany.myapp.service.mapper.UsermMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Userm} entities in the database.
 * The main input is a {@link UsermCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsermDTO} or a {@link Page} of {@link UsermDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsermQueryService extends QueryService<Userm> {
    private final Logger log = LoggerFactory.getLogger(UsermQueryService.class);

    private final UsermRepository usermRepository;

    private final UsermMapper usermMapper;

    private final UsermSearchRepository usermSearchRepository;

    public UsermQueryService(UsermRepository usermRepository, UsermMapper usermMapper, UsermSearchRepository usermSearchRepository) {
        this.usermRepository = usermRepository;
        this.usermMapper = usermMapper;
        this.usermSearchRepository = usermSearchRepository;
    }

    /**
     * Return a {@link List} of {@link UsermDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsermDTO> findByCriteria(UsermCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Userm> specification = createSpecification(criteria);
        return usermMapper.toDto(usermRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsermDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsermDTO> findByCriteria(UsermCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Userm> specification = createSpecification(criteria);
        return usermRepository.findAll(specification, page).map(usermMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsermCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Userm> specification = createSpecification(criteria);
        return usermRepository.count(specification);
    }

    /**
     * Function to convert {@link UsermCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Userm> createSpecification(UsermCriteria criteria) {
        Specification<Userm> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Userm_.id));
            }
            if (criteria.getUserInfoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserInfoId(), root -> root.join(Userm_.userInfo, JoinType.LEFT).get(UserInfo_.id))
                    );
            }
        }
        return specification;
    }
}
