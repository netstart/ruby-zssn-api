package com.github.netstart.zssn.repository;

import org.springframework.stereotype.Repository;

import com.github.netstart.zssn.domain.Survivor;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Survivor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurvivorRepository extends JpaRepository<Survivor, Long>, JpaSpecificationExecutor<Survivor> {

}
