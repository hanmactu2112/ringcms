package com.ringme.cms.service;

import com.ringme.cms.model.TimeQueue;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface TimeQueueService {

    List<TimeQueue> findAllTimeQueue();

    Optional<TimeQueue> findTimeQueueById(Long id);

}
