package com.ringme.cms.repository;

import com.ringme.cms.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission,Long> {
}
