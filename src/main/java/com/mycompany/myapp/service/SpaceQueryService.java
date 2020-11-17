package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Space;
import com.mycompany.myapp.repository.SpaceRepository;
import com.mycompany.myapp.repository.search.SpaceSearchRepository;
import com.mycompany.myapp.service.dto.SpaceCriteria;
import com.mycompany.myapp.service.dto.SpaceDTO;
import com.mycompany.myapp.service.mapper.SpaceMapper;
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
 * Service for executing complex queries for {@link Space} entities in the database.
 * The main input is a {@link SpaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SpaceDTO} or a {@link Page} of {@link SpaceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpaceQueryService extends QueryService<Space> {
    private final Logger log = LoggerFactory.getLogger(SpaceQueryService.class);

    private final SpaceRepository spaceRepository;

    private final SpaceMapper spaceMapper;

    private final SpaceSearchRepository spaceSearchRepository;

    public SpaceQueryService(SpaceRepository spaceRepository, SpaceMapper spaceMapper, SpaceSearchRepository spaceSearchRepository) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
        this.spaceSearchRepository = spaceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SpaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SpaceDTO> findByCriteria(SpaceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Space> specification = createSpecification(criteria);
        return spaceMapper.toDto(spaceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SpaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpaceDTO> findByCriteria(SpaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Space> specification = createSpecification(criteria);
        return spaceRepository.findAll(specification, page).map(spaceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpaceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Space> specification = createSpecification(criteria);
        return spaceRepository.count(specification);
    }

    /**
     * Function to convert {@link SpaceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Space> createSpecification(SpaceCriteria criteria) {
        Specification<Space> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Space_.id));
            }
            if (criteria.getRooms() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRooms(), Space_.rooms));
            }
            if (criteria.getMeters() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMeters(), Space_.meters));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Space_.price));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), Space_.details));
            }
            if (criteria.getUserInfoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserInfoId(), root -> root.join(Space_.userInfo, JoinType.LEFT).get(UserInfo_.id))
                    );
            }
        }
        return specification;
    }
}
