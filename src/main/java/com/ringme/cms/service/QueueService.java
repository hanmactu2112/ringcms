package com.ringme.cms.service;

import com.ringme.cms.model.Department;
import com.ringme.cms.model.Mission;
import com.ringme.cms.model.Queue;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface QueueService {

    List<Queue> findAllQueue();

    void saveQueue(Queue queue);

    Optional<Queue> findQueueById(Long queueId);

    void deleteQueueById(Long queueId);

    Page<Queue> findAllQueuePage(int pageNo,int pageSize);

    Page<Queue> searchQueueByDepartmentTypeMissionProvince(int pageNo, int pageSize, Long departmentId, String type, Long missionId,String province);

    List<Queue> findAllNextQueueById(Long id);
}
