package com.ringme.cms.repository;

import com.ringme.cms.model.RouterRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouterRoleRepository extends JpaRepository<RouterRole,Long> {
}
