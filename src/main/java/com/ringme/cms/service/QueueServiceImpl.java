package com.ringme.cms.service;

import com.ringme.cms.criteria.QueueSearchCriteria;
import com.ringme.cms.criteria.QueueSpecification;
import com.ringme.cms.model.Queue;
import com.ringme.cms.repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    QueueRepository queueRepository;

    @Override
    public List<Queue> findAllQueue() {
        return queueRepository.findAll();
    }

    @Override
    public void saveQueue(Queue queue) {
        queueRepository.save(queue);
    }

    @Override
    public Optional<Queue> findQueueById(Long queueId) {
        return queueRepository.findById(queueId);
    }

    @Override
    public void deleteQueueById(Long queueId) {
        queueRepository.deleteById(queueId);
    }

    @Override
    public Page<Queue> findAllQueuePage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return queueRepository.findAll(pageable);
    }

    @Override
    public Page<Queue> findQueuesByCriteria(QueueSearchCriteria criteria,int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        Specification<Queue> specification = new QueueSpecification(criteria);
        return queueRepository.findAll(specification,pageable);
    }

    @Override
    public List<Queue> findAllNextQueueById(Long id) {
        return queueRepository.findAllByNextQueueId(id);
    }
}
