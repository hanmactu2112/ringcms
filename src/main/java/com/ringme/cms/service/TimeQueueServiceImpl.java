package com.ringme.cms.service;

import com.ringme.cms.model.TimeQueue;
import com.ringme.cms.repository.TimeQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TimeQueueServiceImpl implements TimeQueueService{
    @Autowired
    TimeQueueRepository queueRepository;

    @Override
    public List<TimeQueue> findAllTimeQueue() {
        return queueRepository.findAll();
    }

    @Override
    public Optional<TimeQueue> findTimeQueueById(Long id) {
        return queueRepository.findById(id);
    }
}
