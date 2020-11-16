package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Userm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Userm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsermRepository extends JpaRepository<Userm, Long> {}
