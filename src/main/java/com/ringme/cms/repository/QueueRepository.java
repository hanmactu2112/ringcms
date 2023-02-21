package com.ringme.cms.repository;

import com.ringme.cms.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue,Long> {
}
