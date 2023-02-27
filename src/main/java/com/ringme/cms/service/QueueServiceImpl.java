package com.ringme.cms.service;

import com.ringme.cms.model.Queue;
import com.ringme.cms.repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<Queue> searchQueueByDepartmentTypeMissionProvince(int pageNo, int pageSize, Long departmentId, String type, Long missionId, String province) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        // one
        if (departmentId != null && type == null && missionId == null && province == null) {
            return queueRepository.findAllQueueByDepartment(departmentId, pageable);
        }
        if (departmentId == null && type != null && missionId == null && province == null) {
            return queueRepository.findAllQueueByTypeQueue(type, pageable);
        }
        if (departmentId == null && type == null && missionId != null && province == null) {
            return queueRepository.findAllQueueByMission(missionId, pageable);
        }
        if (departmentId == null && type == null && missionId == null && province != null) {
            return queueRepository.findAllQueueByProvince(province, pageable);
        }

        // two
        if (departmentId != null && type != null && missionId == null && province == null) {
            return queueRepository.findAllQueueByDepartmentAndTypeQueue(departmentId, type, pageable);
        }
        if (departmentId != null && type == null && missionId != null && province == null) {
            return queueRepository.findAllQueueByDepartmentAndMission(departmentId,missionId,pageable);
        }
        if (departmentId != null && type == null && missionId == null && province != null) {
            return queueRepository.findAllQueueByDepartmentProvince(departmentId,province,pageable);
        }
        if (departmentId == null && type != null && missionId != null && province == null) {
            return queueRepository.findAllQueueByTypeQueueAndMission(type,missionId,pageable);
        }
        if (departmentId == null && type != null && missionId == null && province != null) {
            return queueRepository.findAllQueueByTypeQueueAndProvince(type,province,pageable);
        }
        if (departmentId == null && type == null && missionId != null && province != null) {
            return queueRepository.findAllQueueByMissionAndProvince(missionId,province,pageable);
        }

        if (departmentId != null && type == null && missionId == null && province != null) {
//            return queueRepository.findAllQueueByDepartmentAndTypeQueue(departmentId,type,pageable);
        }
        if (departmentId != null && type == null && missionId == null && province != null) {
//            return queueRepository.findAllQueueByDepartmentAndTypeQueue(departmentId,type,pageable);
        }
        if (departmentId != null && type == null && missionId == null && province != null) {
//            return queueRepository.findAllQueueByDepartmentAndTypeQueue(departmentId,type,pageable);
        }

        //four
        if (departmentId != null && type == null && missionId == null && province != null) {
//            return queueRepository.findAllQueueByDepartmentAndTypeQueue(departmentId,type,pageable);
        }

        return null;
    }

    @Override
    public List<Queue> findAllNextQueueById(Long id) {
        return queueRepository.findAllByNextQueueId(id);
    }
}
