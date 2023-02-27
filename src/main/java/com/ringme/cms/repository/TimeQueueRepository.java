package com.ringme.cms.repository;

import com.ringme.cms.model.TimeQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeQueueRepository extends JpaRepository<TimeQueue,Long> {
}
