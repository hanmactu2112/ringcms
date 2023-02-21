package com.ringme.cms.repository;

import com.ringme.cms.model.Router;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouterRepository extends JpaRepository<Router,Long> {
}
