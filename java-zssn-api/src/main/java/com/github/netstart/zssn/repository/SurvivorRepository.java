package com.github.netstart.zssn.repository;

import com.github.netstart.zssn.domain.Survivor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Survivor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurvivorRepository extends JpaRepository<Survivor, Long>, JpaSpecificationExecutor<Survivor> {

}
