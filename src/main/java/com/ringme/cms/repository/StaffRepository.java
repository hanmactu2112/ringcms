package com.ringme.cms.repository;

import com.ringme.cms.model.Queue;
import com.ringme.cms.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {
    @Query(name = "select * from staff s where s.queue_id = queue.id",nativeQuery = true)
    List<Staff> findAllByQueue(Queue queue);
}
