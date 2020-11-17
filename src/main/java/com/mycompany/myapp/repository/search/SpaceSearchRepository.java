package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Space;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Space} entity.
 */
public interface SpaceSearchRepository extends ElasticsearchRepository<Space, Long> {}
