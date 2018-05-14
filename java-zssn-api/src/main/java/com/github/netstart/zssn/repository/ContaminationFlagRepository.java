package com.github.netstart.zssn.repository;

import com.github.netstart.zssn.domain.ContaminationFlag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ContaminationFlag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContaminationFlagRepository extends JpaRepository<ContaminationFlag, Long>, JpaSpecificationExecutor<ContaminationFlag> {

}
