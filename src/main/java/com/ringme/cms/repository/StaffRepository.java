package com.ringme.cms.repository;

import com.ringme.cms.model.Queue;
import com.ringme.cms.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {
    @Query(value = "select * from staff s where s.queue_id = queue.id",nativeQuery = true)
    List<Staff> findAllByQueue(Queue queue);
    @Query(value ="select * from staff s where s.queue_id =?1",
            countQuery = "select count(*) from staff s where s.queue_id =?1",
            nativeQuery = true)
    Page<Staff> findAllByQueueId(Long id, Pageable pageable);
    @Query(value ="select * from staff s where s.user_name in (?1)",
            nativeQuery = true)
    List<Staff> findAllStaffByListUserName(List<String> username);

    @Modifying
    @Query(value = "Update staff s set s.queue_id = ?1 where s.id=?2",nativeQuery = true)
    void addStaffToQueueByIdStaffAndQueueId(Long queueId,Long idStaff);
    @Modifying
    @Query(value = "Update staff s set s.queue_id = null where s.id=?1",nativeQuery = true)
    void removeStaffToQueueByIdStaffAndQueueId(Long idStaff);
    @Query(value = "select * from staff s where s.staff_name like  %?1%",
            countQuery = "select count(*) from staff s where s.staff_name like  %?1%",nativeQuery = true)
    Page<Staff> findStaffFilterByName(String name,Pageable pageable);
    @Query(value = "select * from staff s where s.department_id =  ?1",
            countQuery = "select count(*) from staff s where s.department_id = ?1",nativeQuery = true)
    Page<Staff> findStaffFilterByDepartment(Long id,Pageable pageable);

    @Query(value = "select * from staff s where s.department_id =  ?1 and s.staff_name like  %?2%",nativeQuery = true)
    Page<Staff> findStaffFilterByDepartmentAndName(Long id,String name,Pageable pageable);

    @Query(value = "select * from staff s where s.queue_id is null",countQuery = "select count(*) from staff s where s.queue_id is null",nativeQuery = true)
    Page<Staff> findAllStaffNotInQueue(Pageable pageable);
}
