package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link UserInfo} entity.
 */
public interface UserInfoSearchRepository extends ElasticsearchRepository<UserInfo, Long> {}
