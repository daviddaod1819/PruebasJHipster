package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Userm;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Userm} entity.
 */
public interface UsermSearchRepository extends ElasticsearchRepository<Userm, Long> {}
