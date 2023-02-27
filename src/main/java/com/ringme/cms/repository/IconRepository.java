package com.ringme.cms.repository;

import com.ringme.cms.model.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends JpaRepository<Icon,Long> {
}
